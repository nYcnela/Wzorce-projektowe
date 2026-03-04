"use client";

import { useEffect, useState } from "react";
import { useForm } from "react-hook-form";
import { X } from "lucide-react";
import toast from "react-hot-toast";
import { Button } from "./ui/Button";
import { Input } from "./ui/Input";
import apiClient from "@/lib/apiClient";
import { Car } from "@/types";
import { useAuth } from "@/context/AuthContext";
import { useRouter } from "next/navigation";

interface BookingModalProps {
  car: Car | null;
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void;
}

interface BookingForm {
  startDate: string;
  endDate: string;
}

export default function BookingModal({
  car,
  isOpen,
  onClose,
  onSuccess,
}: BookingModalProps) {
  // Get today's date in YYYY-MM-DD format
  const today = new Date().toISOString().split("T")[0];
  const maxDate = new Date();
  maxDate.setMonth(maxDate.getMonth() + 1);
  const maxDateStr = maxDate.toISOString().split("T")[0];

  const [occupiedDates, setOccupiedDates] = useState<any[]>([]);

  const defaultStartDate = car?.availableFrom || today;

  const {
    register,
    handleSubmit,
    watch,
    reset,
    formState: { errors, isSubmitting },
  } = useForm<BookingForm>({
    defaultValues: {
      startDate: defaultStartDate,
    },
  });

  // Fetch occupied dates
  useEffect(() => {
    if (isOpen && car) {
      apiClient
        .get(`/rentals/car/${car.id}/occupied`)
        .then((res) => setOccupiedDates(res.data))
        .catch((err) => console.error("Failed to fetch occupied dates", err));
    }
  }, [isOpen, car]);

  // Reset form with new default values when car changes
  useEffect(() => {
    if (car) {
      reset({
        startDate: car.availableFrom || today,
        endDate: "",
      });
    }
  }, [car, reset, today]);

  const [totalPrice, setTotalPrice] = useState<number>(0);
  const { isAuthenticated } = useAuth();
  const router = useRouter();

  const startDate = watch("startDate");
  const endDate = watch("endDate");

  const isOverlapping = (start: string, end: string) => {
    return occupiedDates.some((rental) => {
      const rStart = rental.startDate;
      const rEnd = rental.endDate;
      return start <= rEnd && end >= rStart;
    });
  };

  useEffect(() => {
    if (startDate && endDate && car) {
      const start = new Date(startDate);
      const end = new Date(endDate);
      const diffTime = Math.abs(end.getTime() - start.getTime());
      // Liczenie dni włącznie: z 8/01 na 9/01 = 2 dni (8/01 + 9/01)
      let diffDays = Math.ceil(diffTime / (1000 * 60 * 60 * 24)) + 1;

      setTotalPrice(diffDays * car.pricePerDay);
    }
  }, [startDate, endDate, car]);

  if (!isOpen || !car) return null;

  const onSubmit = async (data: BookingForm) => {
    if (!isAuthenticated) {
      toast.error("Musisz być zalogowany, aby wypożyczyć auto");
      router.push("/login");
      return;
    }

    if (isOverlapping(data.startDate, data.endDate)) {
      toast.error("Wybrany termin nakłada się na istniejącą rezerwację!");
      return;
    }

    try {
      await apiClient.post("/rentals", {
        carId: car.id,
        startDate: data.startDate,
        endDate: data.endDate,
      });
      toast.success("Samochód został zarezerwowany!");
      onSuccess();
      onClose();
    } catch (error: any) {
      toast.error(
        error.response?.data?.message ||
          "Nie udało się zarezerwować samochodu. Spróbuj inny termin."
      );
    }
  };

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4 backdrop-blur-sm">
      <div className="relative w-full max-w-md rounded-lg bg-white p-6 shadow-xl">
        <button
          onClick={onClose}
          className="absolute right-4 top-4 text-gray-400 hover:text-gray-600"
        >
          <X size={24} />
        </button>

        <h2 className="mb-2 text-2xl font-bold text-gray-900">
          Rezerwacja {car.brand} {car.model}
        </h2>
        <p className="mb-6 text-gray-600">
          Cena za dzień:{" "}
          <span className="font-semibold text-emerald-600">
            {car.pricePerDay} PLN
          </span>
        </p>

        {occupiedDates.length > 0 && (
          <div className="mb-4 rounded-md bg-amber-50 p-3 text-xs text-amber-800">
            <p className="font-bold mb-1">Zajęte terminy:</p>
            <ul className="list-disc list-inside">
              {occupiedDates.map((r, i) => (
                <li key={i}>
                  {r.startDate} do {r.endDate}
                </li>
              ))}
            </ul>
          </div>
        )}

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <Input
            label="Data odbioru"
            type="date"
            min={today}
            max={maxDateStr}
            {...register("startDate", {
              required: "Data odbioru jest wymagana",
            })}
            error={errors.startDate?.message}
          />

          <Input
            label="Data zwrotu"
            type="date"
            min={startDate || today}
            max={maxDateStr}
            {...register("endDate", { required: "Data zwrotu jest wymagana" })}
            error={errors.endDate?.message}
          />

          {totalPrice > 0 && (
            <div
              className={`rounded-md p-4 ${
                isOverlapping(startDate, endDate)
                  ? "bg-red-50 text-red-900"
                  : "bg-emerald-50 text-emerald-900"
              }`}
            >
              <p className="flex justify-between font-medium">
                <span>Całkowity koszt:</span>
                <span>{totalPrice} PLN</span>
              </p>
              {isOverlapping(startDate, endDate) && (
                <p className="mt-1 text-xs font-bold underline">
                  Termin niedostępny!
                </p>
              )}
            </div>
          )}

          <div className="flex gap-3 pt-4">
            <Button
              type="button"
              variant="outline"
              className="flex-1"
              onClick={onClose}
            >
              Anuluj
            </Button>
            <Button
              type="submit"
              className="flex-1"
              disabled={isSubmitting || totalPrice <= 0}
            >
              {isSubmitting ? "Rezerwowanie..." : "Rezerwuj"}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
