"use client";

import { useEffect, useState } from "react";
import { Button } from "./ui/Button";
import { Input } from "./ui/Input";
import apiClient from "@/lib/apiClient";
import toast from "react-hot-toast";
import { Car } from "@/types";
import { X, Upload, ImageIcon } from "lucide-react";

interface EditCarModalProps {
  isOpen: boolean;
  onClose: () => void;
  onSuccess: () => void;
  car: Car | null;
}

export default function EditCarModal({
  isOpen,
  onClose,
  onSuccess,
  car,
}: EditCarModalProps) {
  const [formData, setFormData] = useState({
    brand: "",
    model: "",
    productionYear: "",
    color: "",
    pricePerDay: "",
    available: true,
  });

  const [loading, setLoading] = useState(false);
  const [imageFile, setImageFile] = useState<File | null>(null);
  const [imagePreview, setImagePreview] = useState<string | null>(null);

  // Populate form when car changes
  useEffect(() => {
    if (car) {
      setFormData({
        brand: car.brand || "",
        model: car.model || "",
        productionYear: car.productionYear?.toString() || "",
        color: car.color || "",
        pricePerDay: car.pricePerDay?.toString() || "",
        available: car.available ?? true,
      });
      setImagePreview(car.imageUrl || null);
      setImageFile(null);
    }
  }, [car]);

  const handleImageChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const file = e.target.files?.[0];
    if (file) {
      setImageFile(file);
      const reader = new FileReader();
      reader.onloadend = () => {
        setImagePreview(reader.result as string);
      };
      reader.readAsDataURL(file);
    }
  };

  const handleChange = (
    e: React.ChangeEvent<HTMLInputElement | HTMLSelectElement>
  ) => {
    const { name, value, type } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]:
        type === "checkbox" ? (e.target as HTMLInputElement).checked : value,
    }));
  };

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!car) return;

    setLoading(true);

    try {
      const carData = {
        brand: formData.brand,
        model: formData.model,
        productionYear: parseInt(formData.productionYear),
        color: formData.color,
        pricePerDay: parseFloat(formData.pricePerDay),
        available: formData.available,
      };

      const formDataToSend = new FormData();
      formDataToSend.append(
        "car",
        new Blob([JSON.stringify(carData)], { type: "application/json" })
      );

      if (imageFile) {
        formDataToSend.append("image", imageFile);
      }

      await apiClient.put(`/cars/${car.id}`, formDataToSend, {
        headers: {
          "Content-Type": "multipart/form-data",
        },
      });

      toast.success("Samochód został zaktualizowany!");
      onSuccess();
      onClose();
    } catch (error: any) {
      toast.error(
        error.response?.data?.message || "Nie udało się zaktualizować samochodu"
      );
    } finally {
      setLoading(false);
    }
  };

  if (!isOpen || !car) return null;

  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black/50 p-4">
        <div className="relative w-full max-w-md max-h-[90vh] overflow-y-auto rounded-lg bg-white p-6 shadow-xl">
        <button
          onClick={onClose}
          className="absolute right-4 top-4 text-gray-400 hover:text-gray-600"
        >
          <X size={24} />
        </button>

        <h2 className="mb-6 text-2xl font-bold text-gray-900">
          Edytuj samochód
        </h2>

        <form onSubmit={handleSubmit} className="space-y-4">
          <div className="flex flex-col items-center justify-center gap-4 py-4">
            <div className="relative h-32 w-full max-w-[200px] overflow-hidden rounded-lg border-2 border-dashed border-gray-300 bg-gray-50 flex items-center justify-center">
              {imagePreview ? (
                <img
                  src={imagePreview}
                  alt="Podgląd"
                  className="h-full w-full object-contain"
                />
              ) : (
                <div className="flex flex-col items-center text-gray-400">
                  <ImageIcon size={32} />
                  <span className="text-xs">Brak zdjęcia</span>
                </div>
              )}
            </div>
            <label className="cursor-pointer">
              <span className="inline-flex items-center gap-2 rounded-md bg-emerald-50 px-3 py-2 text-sm font-medium text-emerald-700 hover:bg-emerald-100 transition-colors border border-emerald-200">
                <Upload size={16} />
                Zmień zdjęcie
              </span>
              <input
                type="file"
                className="hidden"
                accept="image/*"
                onChange={handleImageChange}
              />
            </label>
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Marka
            </label>
            <Input
              name="brand"
              value={formData.brand}
              onChange={handleChange}
              required
              placeholder="np. Toyota"
            />
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Model
            </label>
            <Input
              name="model"
              value={formData.model}
              onChange={handleChange}
              required
              placeholder="np. Corolla"
            />
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Rok produkcji
            </label>
            <Input
              type="number"
              name="productionYear"
              value={formData.productionYear}
              onChange={handleChange}
              required
              min="1900"
              max={new Date().getFullYear() + 1}
            />
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Kolor
            </label>
            <Input
              name="color"
              value={formData.color}
              onChange={handleChange}
              required
              placeholder="np. Czerwony"
            />
          </div>

          <div>
            <label className="mb-1 block text-sm font-medium text-gray-700">
              Cena za dzień (PLN)
            </label>
            <Input
              type="number"
              name="pricePerDay"
              value={formData.pricePerDay}
              onChange={handleChange}
              required
              min="0"
              step="0.01"
            />
          </div>

          <div className="flex items-center gap-2">
            <input
              type="checkbox"
              id="available"
              name="available"
              checked={formData.available}
              onChange={handleChange}
              className="h-4 w-4 rounded border-gray-300 text-emerald-600 focus:ring-emerald-500"
            />
            <label
              htmlFor="available"
              className="text-sm font-medium text-gray-700"
            >
              Dostępny
            </label>
          </div>

          <div className="flex gap-3 pt-4">
            <Button
              type="button"
              variant="outline"
              onClick={onClose}
              className="flex-1"
              disabled={loading}
            >
              Anuluj
            </Button>
            <Button type="submit" className="flex-1" disabled={loading}>
              {loading ? "Zapisywanie..." : "Zapisz zmiany"}
            </Button>
          </div>
        </form>
      </div>
    </div>
  );
}
