"use client";

import { useEffect, useState } from "react";
import { useRouter } from "next/navigation";
import { useAuth } from "@/context/AuthContext";
import apiClient from "@/lib/apiClient";
import { Button } from "@/components/ui/Button";
import { Navbar } from "@/components/Navbar";
import { Trash2, User } from "lucide-react";
import toast from "react-hot-toast";

interface UserResponse {
  id: number;
  email: string;
  role: string;
}

export default function AdminUsersPage() {
  const { user, isAdmin, isLoading } = useAuth();
  const router = useRouter();
  const [users, setUsers] = useState<UserResponse[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!isLoading && !isAdmin) {
      router.push("/dashboard");
    }
  }, [isLoading, isAdmin, router]);

  const fetchUsers = async () => {
    try {
      const response = await apiClient.get("/users");
      setUsers(response.data);
    } catch (error) {
      console.error("Failed to fetch users", error);
      toast.error("Nie udało się pobrać listy użytkowników.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    if (isAdmin) {
      fetchUsers();
    }
  }, [isAdmin]);

  const handleDelete = async (userId: number) => {
    if (!confirm("Czy na pewno chcesz usunąć tego użytkownika?")) return;

    try {
      await apiClient.delete(`/users/${userId}`);
      toast.success("Użytkownik został usunięty.");
      setUsers(users.filter((u) => u.id !== userId));
    } catch (error) {
      console.error(error);
      toast.error("Nie udało się usunąć użytkownika.");
    }
  };

  if (isLoading || loading) {
    return (
      <div className="flex h-screen items-center justify-center">
        <div className="text-lg">Ładowanie...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen">
      <div className="container mx-auto p-4 md:p-8">
        <div className="mb-8 flex items-center justify-between">
          <h1 className="text-3xl font-bold text-gray-900">
            Zarządzanie Użytkownikami
          </h1>
        </div>

        <div className="rounded-xl border bg-white p-6 shadow-sm overflow-x-auto">
          <table className="w-full text-left">
            <thead>
              <tr className="border-b text-gray-500">
                <th className="pb-3 font-medium">ID</th>
                <th className="pb-3 font-medium">Email</th>
                <th className="pb-3 font-medium">Rola</th>
                <th className="pb-3 font-medium text-right">Akcje</th>
              </tr>
            </thead>
            <tbody className="divide-y">
              {users.map((u) => (
                <tr key={u.id} className="group hover:bg-gray-50">
                  <td className="py-4 text-gray-600">{u.id}</td>
                  <td className="py-4 font-medium text-gray-900">{u.email}</td>
                  <td className="py-4">
                    <span
                      className={`rounded-full px-2 py-1 text-xs font-semibold ${
                        u.role === "ADMIN"
                          ? "bg-purple-100 text-purple-800"
                          : "bg-blue-100 text-blue-800"
                      }`}
                    >
                      {u.role}
                    </span>
                  </td>
                  <td className="py-4 text-right">
                    {u.role !== "ADMIN" && (
                      <Button
                        variant="outline"
                        size="sm"
                        onClick={() => handleDelete(u.id)}
                        className="!text-red-600 hover:!bg-red-50 hover:!text-red-700 !border-red-400 !border-2"
                      >
                        <Trash2 size={16} />
                      </Button>
                    )}
                  </td>
                </tr>
              ))}
              {users.length === 0 && (
                <tr>
                  <td colSpan={4} className="py-8 text-center text-gray-500">
                    Brak użytkowników.
                  </td>
                </tr>
              )}
            </tbody>
          </table>
        </div>
      </div>
    </div>
  );
}
