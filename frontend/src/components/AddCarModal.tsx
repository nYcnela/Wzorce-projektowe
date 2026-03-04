"use client";

import { useForm } from "react-hook-form";
import { X } from "lucide-react";
import toast from "react-hot-toast";
import { Button } from "./ui/Button";
import { Input } from "./ui/Input";
import apiClient from "@/lib/apiClient";

interface AddCarModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void;
}

interface AddCarForm {
  brand: string;
  model: string;
  productionYear: number;
  color: string;
  pricePerDay: number;
  image: FileList;
}

export default function AddCarModal({
  isOpen,
  onClose,
  onSuccess,
}: AddCarModalProps) {
  const {
    register,
    handleSubmit,
    reset,
    watch,
    formState: { errors, isSubmitting },
  } = useForm<AddCarForm>();

  const watchImage = watch("image");

  if (!isOpen) return null;

  const onSubmit = async (data: AddCarForm) => {
    try {
      const formData = new FormData();

      const carData = {
        brand: data.brand,
        model: data.model,
        productionYear: data.productionYear,
        color: data.color,
        pricePerDay: data.pricePerDay,
      };

      // Append car data as a JSON blob. Note: 'car' part must match @RequestPart("car")
      formData.append(
        "car",
        new Blob([JSON.stringify(carData)], { type: "application/json" })
      );

      // Append file if selected
      if (data.image && data.image.length > 0) {
        formData.append("image", data.image[0]);
      }

      await apiClient.post("/cars", formData); // Axios automatically sets multipart/form-data with boundary

      toast.success("Samochód został dodany!");
      reset();
      onSuccess();
      onClose();
    } catch (error: any) {
      console.error(error);
      toast.error("Nie udało się dodać samochodu.");
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

        <h2 className="mb-6 text-2xl font-bold text-gray-900">
          Dodaj nowy samochód
        </h2>

        <form onSubmit={handleSubmit(onSubmit)} className="space-y-4">
          <Input
            label="Marka"
            placeholder="np. Toyota"
            {...register("brand", { required: "Marka jest wymagana" })}
            error={errors.brand?.message}
          />

          <Input
            label="Model"
            placeholder="np. Corolla"
            {...register("model", { required: "Model jest wymagany" })}
            error={errors.model?.message}
          />

          <div className="grid grid-cols-2 gap-4">
            <Input
              label="Rok produkcji"
              type="number"
              placeholder="2023"
              {...register("productionYear", {
                required: "Rok jest wymagany",
                min: { value: 1900, message: "Nieprawidłowy rok" },
                max: { value: 2025, message: "Nieprawidłowy rok" },
              })}
              error={errors.productionYear?.message}
            />

            <Input
              label="Kolor"
              placeholder="np. Czarny"
              {...register("color", { required: "Kolor jest wymagany" })}
              error={errors.color?.message}
            />
          </div>

          <Input
            label="Cena za dzień (PLN)"
            type="number"
            placeholder="150"
            {...register("pricePerDay", {
              required: "Cena jest wymagana",
              min: 1,
            })}
            error={errors.pricePerDay?.message}
          />

          <div className="space-y-1">
            <label className="block text-sm font-medium text-gray-700">
              Zdjęcie samochodu
            </label>
            <div className="flex items-center gap-3">
              <label className="cursor-pointer bg-emerald-50 text-emerald-700 px-4 py-2 rounded-md text-sm font-semibold hover:bg-emerald-100 transition-all border border-emerald-200">
                Wybierz plik
                <input
                  type="file"
                  accept="image/*"
                  {...register("image")}
                  className="hidden"
                />
              </label>
              <span className="text-sm text-gray-500 truncate max-w-[200px]">
                {watchImage && watchImage[0]
                  ? watchImage[0].name
                  : "Nie wybrano pliku"}
              </span>
            </div>
          </div>

          <div className="flex gap-3 pt-4">
            <Button
              type="button"
              variant="outline"
              className="flex-1"
              onClick={onClose}
            >
              Anuluj
            </Button>
            <Button type="submit" className="flex-1" disabled={isSubmitting}>
              {isSubmitting ? "Dodawanie..." : "Dodaj"}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
