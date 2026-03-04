"use client";

import { useEffect, useState } from "react";
import apiClient from "@/lib/apiClient";
import { Rental } from "@/types";
import { Button } from "@/components/ui/Button";
import toast from "react-hot-toast";
import { Calendar, CheckCircle, Clock, XCircle } from "lucide-react";
import { useAuth } from "@/context/AuthContext";
import { useRouter } from "next/navigation";

export default function DashboardPage() {
  const [rentals, setRentals] = useState<Rental[]>([]);
  const [loading, setLoading] = useState(true);
  const { isAuthenticated } = useAuth();
  const router = useRouter();

  useEffect(() => {
    if (!isAuthenticated) {
      router.push("/login");
      return;
    }

    fetchRentals();
  }, [isAuthenticated, router]);

  const fetchRentals = async () => {
    try {
      const response = await apiClient.get<Rental[]>("/rentals/my-rentals");
      setRentals(response.data);
    } catch (error) {
      toast.error("Nie udało się pobrać historii wypożyczeń", {
        id: "fetch_rentals_error",
      });
    } finally {
      setLoading(false);
    }
  };

  const handleReturn = async (id: number) => {
    if (
      !window.confirm(
        "Czy na pewno chcesz zwrócić samochód? Zostanie naliczona opłata za faktyczny czas wypożyczenia."
      )
    )
      return;

    try {
      await apiClient.post(`/rentals/${id}/return`);
      toast.success("Samochód został zwrócony. Opłata została naliczona.");
      fetchRentals();
    } catch (error) {
      toast.error("Nie udało się zwrócić samochodu");
    }
  };

  const handleCancel = async (id: number) => {
    if (
      !window.confirm(
        "Czy na pewno chcesz anulować rezerwację? Nie zostanie pobrana żadna opłata."
      )
    )
      return;

    try {
      await apiClient.post(`/rentals/${id}/cancel`);
      toast.success("Rezerwacja została anulowana. Brak opłaty.");
      fetchRentals();
    } catch (error: any) {
      toast.error(
        error.response?.data?.message || "Nie udało się anulować rezerwacji"
      );
    }
  };

  // Pobiera dzisiejszą datę jako string YYYY-MM-DD (bez problemów ze strefami czasowymi)
  const getTodayString = () => {
    const today = new Date();
    return today.toISOString().split("T")[0]; // "2026-01-10"
  };

  // Sprawdza czy rezerwacja jeszcze się nie rozpoczęła (można anulować bez opłaty)
  const canCancel = (rental: Rental) => {
    if (rental.status !== "ACTIVE") return false;
    const todayStr = getTodayString();
    return rental.startDate > todayStr; // Data startu jest w przyszłości
  };

  // Sprawdza czy rezerwacja już trwa (można zwrócić z opłatą)
  const canReturn = (rental: Rental) => {
    if (rental.status !== "ACTIVE") return false;
    const todayStr = getTodayString();
    return rental.startDate <= todayStr; // Data startu jest dzisiaj lub w przeszłości
  };

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

  if (loading) {
    return (
      <div className="flex h-64 items-center justify-center">
        <div className="h-8 w-8 animate-spin rounded-full border-4 border-emerald-600 border-t-transparent"></div>
      </div>
    );
  }

  return (
    <div className="container mx-auto max-w-5xl px-4 py-8">
      <h1 className="mb-8 text-3xl font-bold text-gray-900">
        Twoje wypożyczenia
      </h1>

      {rentals.length === 0 ? (
        <div className="rounded-lg border border-dashed border-gray-300 p-12 text-center">
          <p className="text-lg text-gray-600">
            Nie masz jeszcze żadnych wypożyczeń.
          </p>
        </div>
      ) : (
        <div className="grid gap-6">
          {rentals.map((rental) => (
            <div
              key={rental.id}
              className="flex flex-col justify-between gap-4 rounded-lg border bg-white p-6 shadow-sm sm:flex-row sm:items-center"
            >
              <div className="space-y-1">
                <div className="flex items-center gap-3">
                  <h3 className="text-xl font-semibold text-gray-900">
                    {rental.carBrand} {rental.carModel}
                  </h3>
                  {getStatusBadge(rental.status)}
                </div>

                <div className="flex flex-wrap gap-4 text-sm text-gray-600">
                  <div className="flex items-center gap-1">
                    <Calendar size={16} />
                    <span>
                      {rental.startDate} - {rental.endDate}
                    </span>
                  </div>
                  <div className="font-medium text-emerald-600">
                    {rental.status === "CANCELLED" ? (
                      <span className="text-gray-400">Brak opłaty</span>
                    ) : (
                      <>Koszt: {rental.totalCost} PLN</>
                    )}
                  </div>
                </div>
              </div>

              <div className="flex flex-col gap-2 sm:flex-row">
                {canReturn(rental) && (
                  <Button
                    variant="outline"
                    onClick={() => handleReturn(rental.id)}
                    className="w-full sm:w-auto"
                  >
                    Zwróć samochód
                  </Button>
                )}

                {canCancel(rental) && (
                  <Button
                    variant="outline"
                    onClick={() => handleCancel(rental.id)}
                    className="w-full border-red-300 text-red-600 hover:bg-red-50 sm:w-auto"
                  >
                    Anuluj (bez opłaty)
                  </Button>
                )}
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
