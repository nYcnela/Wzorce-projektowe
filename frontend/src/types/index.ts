export interface Car {
  id: number;
  brand: string;
  model: string;
  productionYear: number;
  color: string;
  pricePerDay: number;
  available: boolean;
  imageUrl?: string;
  availableFrom?: string;
}

export interface Rental {
  id: number;
  carId: number;
  userId: number;
  startDate: string;
  endDate: string;
  totalCost: number;
  status: "PENDING" | "CONFIRMED" | "ACTIVE" | "COMPLETED" | "CANCELLED";
  carBrand: string;
  carModel: string;
}

export interface User {
  id: number;
  email: string;
  role: "USER" | "ADMIN";
}
