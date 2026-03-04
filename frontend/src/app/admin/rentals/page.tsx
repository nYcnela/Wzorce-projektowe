"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";
import apiClient from "@/lib/apiClient";
import { Button } from "@/components/ui/Button";
import { Calendar, CheckCircle, Clock, XCircle, User, Car } from "lucide-react";
import toast from "react-hot-toast";

interface Rental {
  id: number;
  userId: number;
  userEmail: string;
  carId: number;
  carBrand: string;
  carModel: string;
  startDate: string;
  endDate: string;
  totalCost: number;
  status: "ACTIVE" | "COMPLETED" | "CANCELLED";
}

export default function AdminRentalsPage() {
  const { isAdmin, isLoading } = useAuth();
  const router = useRouter();
  const [rentals, setRentals] = useState<Rental[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!isLoading && !isAdmin) {
      router.push("/dashboard");
    }
  }, [isLoading, isAdmin, router]);

  const fetchRentals = async () => {
    try {
      const response = await apiClient.get("/rentals");
      setRentals(response.data);
    } catch (error) {
      console.error("Failed to fetch rentals", error);
      toast.error("Nie udało się pobrać listy wypożyczeń.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (isAdmin) {
      fetchRentals();
    }
  }, [isAdmin]);

  const getStatusBadge = (status: Rental["status"]) => {
    switch (status) {
      case "ACTIVE":
        return (
          <span className="inline-flex items-center gap-1 rounded-full bg-blue-100 px-2.5 py-0.5 text-xs font-medium text-blue-800">
            <Clock size={12} /> Aktywne
          </span>
        );
      case "COMPLETED":
        return (
          <span className="inline-flex items-center gap-1 rounded-full bg-emerald-100 px-2.5 py-0.5 text-xs font-medium text-emerald-800">
            <CheckCircle size={12} /> Zakończone
          </span>
        );
      case "CANCELLED":
        return (
          <span className="inline-flex items-center gap-1 rounded-full bg-red-100 px-2.5 py-0.5 text-xs font-medium text-red-800">
            <XCircle size={12} /> Anulowane
          </span>
        );
      default:
        return null;
    }
  };

  if (isLoading || loading) {
    return (
      <div className="flex h-screen items-center justify-center">
        <div className="h-8 w-8 animate-spin rounded-full border-4 border-emerald-600 border-t-transparent"></div>
      </div>
    );
  }

  return (
    <div className="min-h-screen">
      <div className="container mx-auto p-4 md:p-8">
        <div className="mb-8 flex items-center justify-between">
          <h1 className="text-3xl font-bold text-gray-900">
            Zarządzanie Wypożyczeniami
          </h1>
        </div>

        <div className="rounded-xl border bg-white p-6 shadow-sm overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="border-b text-gray-500">
                <th className="pb-3 font-medium">ID</th>
                <th className="pb-3 font-medium">Użytkownik</th>
                <th className="pb-3 font-medium">Samochód</th>
                <th className="pb-3 font-medium">Daty</th>
                <th className="pb-3 font-medium">Koszt</th>
                <th className="pb-3 font-medium">Status</th>
              </tr>
            </thead>
            <tbody className="divide-y">
              {rentals.map((rental) => (
                <tr key={rental.id} className="group hover:bg-gray-50">
                  <td className="py-4 text-gray-600">{rental.id}</td>
                  <td className="py-4">
                    <div className="flex items-center gap-2">
                      <User size={16} className="text-gray-400" />
                      <span className="text-sm text-gray-900">
                        {rental.userEmail}
                      </span>
                    </div>
                  </td>
                  <td className="py-4">
                    <div className="flex items-center gap-2">
                      <Car size={16} className="text-gray-400" />
                      <span className="font-medium text-gray-900">
                        {rental.carBrand} {rental.carModel}
                      </span>
                    </div>
                  </td>
                  <td className="py-4">
                    <div className="flex items-center gap-1 text-sm text-gray-600">
                      <Calendar size={14} />
                      <span>
                        {rental.startDate} - {rental.endDate}
                      </span>
                    </div>
                  </td>
                  <td className="py-4 font-semibold text-emerald-600">
                    {rental.totalCost} PLN
                  </td>
                  <td className="py-4">{getStatusBadge(rental.status)}</td>
                </tr>
              ))}
              {rentals.length === 0 && (
                <tr>
                  <td colSpan={6} className="py-8 text-center text-gray-500">
                    Brak wypożyczeń.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>

        <div className="mt-6 rounded-lg bg-blue-50 p-4">
          <h3 className="mb-2 font-semibold text-blue-900">Statystyki:</h3>
          <div className="grid grid-cols-1 gap-4 sm:grid-cols-3">
            <div className="rounded-lg bg-white p-4 shadow-sm">
              <div className="text-sm text-gray-500">Aktywne</div>
              <div className="text-2xl font-bold text-blue-600">
                {rentals.filter((r) => r.status === "ACTIVE").length}
              </div>
            </div>
            <div className="rounded-lg bg-white p-4 shadow-sm">
              <div className="text-sm text-gray-500">Zakończone</div>
              <div className="text-2xl font-bold text-emerald-600">
                {rentals.filter((r) => r.status === "COMPLETED").length}
              </div>
            </div>
            <div className="rounded-lg bg-white p-4 shadow-sm">
              <div className="text-sm text-gray-500">Całkowity przychód</div>
              <div className="text-2xl font-bold text-gray-900">
                {rentals
                  .filter((r) => r.status === "COMPLETED")
                  .reduce((sum, r) => sum + r.totalCost, 0)}{" "}
                PLN
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
