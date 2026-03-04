"use client";

import { Car } from "@/types";
import { Button } from "./ui/Button";
import { useAuth } from "@/context/AuthContext";
import {
  CarFront,
  Calendar,
  Palette,
  Heart,
  Pencil,
  Trash2,
} from "lucide-react";

interface CarCardProps {
  car: Car;
  isFavorite?: boolean;
  onRent: (car: Car) => void;
  onDelete?: (id: number) => void;
  onEdit?: (car: Car) => void;
  onToggleFavorite?: (id: number) => void;
}

export default function CarCard({
  car,
  isFavorite = false,
  onRent,
  onDelete,
  onEdit,
  onToggleFavorite,
}: CarCardProps) {
  const { isAdmin, isAuthenticated } = useAuth();

  return (
    <div className="flex flex-col rounded-xl border bg-white p-6 shadow-sm transition-all hover:shadow-md relative">
      {isAuthenticated && onToggleFavorite && (
        <button
          onClick={(e) => {
            e.stopPropagation();
            onToggleFavorite(car.id);
          }}
          className="absolute top-4 right-4 z-10 rounded-full bg-white/80 p-2 text-gray-400 hover:text-red-500 hover:bg-white transition-colors"
        >
          <Heart
            size={20}
            className={isFavorite ? "fill-red-500 text-red-500" : ""}
          />
        </button>
      )}
      <div className="mb-4 flex h-48 items-center justify-center rounded-lg bg-gray-50 text-gray-400 overflow-hidden relative">
        {car.imageUrl ? (
          <img
            src={car.imageUrl}
            alt={`${car.brand} ${car.model}`}
            className="h-full w-full object-contain"
          />
        ) : (
          <CarFront size={64} />
        )}
      </div>

      <div className="mb-2 flex items-start justify-between">
        <div>
          <h3 className="text-xl font-bold text-gray-900">
            {car.brand} {car.model}
          </h3>
          <span
            className={`inline-flex items-center rounded-full px-2.5 py-0.5 text-xs font-medium ${
              car.available
                ? "bg-emerald-100 text-emerald-800"
                : "bg-red-100 text-red-800"
            }`}
          >
            {car.available ? "Dostępny" : "Niedostępny"}
          </span>
          {!car.available && car.availableFrom && (
            <p className="mt-1 text-xs text-gray-500 italic">
              Dostępny od: {car.availableFrom}
            </p>
          )}
        </div>
        <div className="text-right">
          <p className="text-lg font-bold text-emerald-600">
            {car.pricePerDay} PLN
          </p>
          <p className="text-xs text-gray-500">za dzień</p>
        </div>
      </div>

      <div className="mb-6 space-y-2 text-sm text-gray-600">
        <div className="flex items-center gap-2">
          <Calendar size={16} />
          <span>Rok produkcji: {car.productionYear}</span>
        </div>
        <div className="flex items-center gap-2">
          <Palette size={16} />
          <span>Kolor: {car.color}</span>
        </div>
      </div>

      <div className="mt-auto flex gap-2">
        <Button className="flex-1" onClick={() => onRent(car)}>
          {car.available ? "Wypożycz" : "Zarezerwuj"}
        </Button>

        {isAdmin && (
          <>
            {onEdit && (
              <Button
                variant="outline"
                className="text-emerald-600 hover:bg-emerald-50 hover:text-emerald-700"
                onClick={() => onEdit(car)}
              >
                <Pencil size={16} />
              </Button>
            )}
            {onDelete && (
              <Button
                variant="outline"
                className="text-red-600 hover:bg-red-50 hover:text-red-700"
                onClick={() => onDelete(car.id)}
              >
                <Trash2 size={16} />
              </Button>
            )}
          </>
        )}
      </div>
    </div>
  );
}
