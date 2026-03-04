"use client";

import { useEffect, useState } from "react";
import apiClient from "@/lib/apiClient";
import { Car } from "@/types";
import CarCard from "@/components/CarCard";
import BookingModal from "@/components/BookingModal";
import AddCarModal from "@/components/AddCarModal";
import EditCarModal from "@/components/EditCarModal";
import { useAuth } from "@/context/AuthContext";
import { Button } from "@/components/ui/Button";
import { Plus, Heart, ArrowUpDown } from "lucide-react";
import toast from "react-hot-toast";

export default function HomePage() {
  const [cars, setCars] = useState<Car[]>([]);
  const [loading, setLoading] = useState(true);
  const [selectedCar, setSelectedCar] = useState<Car | null>(null);
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [isAddCarModalOpen, setIsAddCarModalOpen] = useState(false);
  const [isEditCarModalOpen, setIsEditCarModalOpen] = useState(false);
  const [carToEdit, setCarToEdit] = useState<Car | null>(null);

  // Favorites logic
  const [favoriteIds, setFavoriteIds] = useState<Set<number>>(new Set());
  const [showFavoritesOnly, setShowFavoritesOnly] = useState(false);

  // Sorting logic
  const [sortBy, setSortBy] = useState<
    "none" | "price-asc" | "price-desc" | "favorites"
  >("none");

  const { isAdmin, isAuthenticated } = useAuth();

  const fetchCars = async () => {
    try {
      const params: any = {};
      if (sortBy !== "none" && sortBy !== "favorites") {
        params.sortBy = sortBy;
      }
      const response = await apiClient.get<Car[]>("/cars", { params });
      setCars(response.data);
    } catch (error) {
      toast.error("Nie udało się pobrać listy samochodów", {
        id: "fetch_cars_error",
      });
    } finally {
      setLoading(false);
    }
  };

  const fetchFavorites = async () => {
    if (!isAuthenticated) return;
    try {
      const response = await apiClient.get<Car[]>("/users/favorites");
      setFavoriteIds(new Set(response.data.map((c) => c.id)));
    } catch (error) {
      console.error("Failed to fetch favorites", error);
    }
  };

  useEffect(() => {
    fetchCars();
    if (isAuthenticated) fetchFavorites();
  }, [isAuthenticated, sortBy]); // Re-fetch on auth change or sort change

  const handleRent = (car: Car) => {
    setSelectedCar(car);
    setIsModalOpen(true);
  };

  const handleDelete = async (id: number) => {
    if (!window.confirm("Czy na pewno chcesz usunąć ten samochód?")) return;

    try {
      await apiClient.delete(`/cars/${id}`);
      toast.success("Samochód został usunięty");
      fetchCars();
    } catch (error) {
      toast.error("Nie udało się usunąć samochodu");
    }
  };

  const handleEdit = (car: Car) => {
    setCarToEdit(car);
    setIsEditCarModalOpen(true);
  };

  const handleToggleFavorite = async (id: number) => {
    if (!isAuthenticated) {
      toast.error("Zaloguj się, aby dodać do ulubionych");
      return;
    }

    try {
      // Optimistic update
      const newFavorites = new Set(favoriteIds);
      if (newFavorites.has(id)) {
        newFavorites.delete(id);
      } else {
        newFavorites.add(id);
      }
      setFavoriteIds(newFavorites);

      await apiClient.post(`/users/favorites/${id}`);
    } catch (error) {
      toast.error("Wystąpił błąd przy aktualizacji ulubionych");
      fetchFavorites(); // Revert on error
    }
  };

  const handleBookingSuccess = () => {
    fetchCars(); // Refresh availability
  };

  const displayedCars = showFavoritesOnly
    ? cars.filter((c) => favoriteIds.has(c.id))
    : cars;

  // Apply client-side sorting only for favorites
  const sortedCars =
    sortBy === "favorites"
      ? [...displayedCars].sort((a, b) => {
          const aIsFav = favoriteIds.has(a.id) ? 1 : 0;
          const bIsFav = favoriteIds.has(b.id) ? 1 : 0;
          return bIsFav - aIsFav; // Favorites first
        })
      : displayedCars;

  return (
    <div className="flex flex-col gap-8 pb-10">
      {/* Hero Section */}
      <section className="relative -mx-4 -mt-8 bg-emerald-900 px-4 py-20 text-center sm:-mx-6 sm:px-6 md:-mx-8 lg:px-8">
        <div className="mx-auto max-w-3xl space-y-4">
          <h1 className="text-4xl font-bold tracking-tight text-white sm:text-6xl">
            Znajdź swoje wymarzone auto
          </h1>
          <p className="mx-auto max-w-xl text-lg text-emerald-100">
            Szeroki wybór samochodów w najlepszych cenach. Rezerwuj online w
            kilka minut.
          </p>
        </div>
      </section>

      {/* Main Content */}
      <div className="container mx-auto px-4">
        <div className="mb-8 flex flex-col gap-4 sm:flex-row sm:items-center sm:justify-between">
          <h2 className="text-2xl font-bold text-gray-900">
            Dostępne samochody
          </h2>

          <div className="flex flex-wrap items-center gap-2">
            {/* Sorting Dropdown */}
            <div className="relative">
              <select
                value={sortBy}
                onChange={(e) => setSortBy(e.target.value as typeof sortBy)}
                className="appearance-none rounded-md border border-gray-300 bg-white px-4 py-2 pr-10 text-sm font-medium text-gray-700 hover:border-gray-400 focus:border-emerald-500 focus:outline-none focus:ring-2 focus:ring-emerald-500"
              >
                <option value="none">Sortuj według</option>
                <option value="price-asc">Cena: Od najniższej</option>
                <option value="price-desc">Cena: Od najwyższej</option>
                {isAuthenticated && (
                  <option value="favorites">Ulubione na górze</option>
                )}
              </select>
              <ArrowUpDown
                size={16}
                className="pointer-events-none absolute right-3 top-1/2 -translate-y-1/2 text-gray-500"
              />
            </div>

            {isAuthenticated && (
              <Button
                variant={showFavoritesOnly ? "primary" : "outline"}
                className="flex items-center gap-2"
                onClick={() => setShowFavoritesOnly(!showFavoritesOnly)}
              >
                <Heart
                  size={20}
                  className={showFavoritesOnly ? "fill-white" : ""}
                />
                {showFavoritesOnly ? "Pokaż wszystkie" : "Pokaż ulubione"}
              </Button>
            )}

            {isAdmin && (
              <Button
                className="flex items-center gap-2"
                onClick={() => setIsAddCarModalOpen(true)}
              >
                <Plus size={20} />
                Dodaj samochód
              </Button>
            )}
          </div>
        </div>

        {loading ? (
          <div className="flex h-64 items-center justify-center">
            <div className="h-8 w-8 animate-spin rounded-full border-4 border-emerald-600 border-t-transparent"></div>
          </div>
        ) : sortedCars.length > 0 ? (
          <div className="grid gap-6 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-4">
            {sortedCars.map((car) => (
              <CarCard
                key={car.id}
                car={car}
                isFavorite={favoriteIds.has(car.id)}
                onToggleFavorite={handleToggleFavorite}
                onRent={handleRent}
                onDelete={isAdmin ? handleDelete : undefined}
                onEdit={isAdmin ? handleEdit : undefined}
              />
            ))}
          </div>
        ) : (
          <div className="flex h-64 flex-col items-center justify-center text-gray-500">
            <p className="text-lg">
              {showFavoritesOnly
                ? "Brak ulubionych samochodów."
                : "Brak dostępnych samochodów."}
            </p>
          </div>
        )}
      </div>

      <BookingModal
        car={selectedCar}
        isOpen={isModalOpen}
        onClose={() => setIsModalOpen(false)}
        onSuccess={handleBookingSuccess}
      />

      <AddCarModal
        isOpen={isAddCarModalOpen}
        onClose={() => setIsAddCarModalOpen(false)}
        onSuccess={fetchCars}
      />

      <EditCarModal
        isOpen={isEditCarModalOpen}
        onClose={() => setIsEditCarModalOpen(false)}
        onSuccess={fetchCars}
        car={carToEdit}
      />
    </div>
  );
}
