"use client";

import { useForm } from "react-hook-form";
import apiClient from "@/lib/apiClient";
import { useAuth } from "@/context/AuthContext";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import toast from "react-hot-toast";
import { useRouter } from "next/navigation";
import Link from "next/link";

interface LoginForm {
  email: string;
  password: string;
}

export default function LoginPage() {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<LoginForm>();
  const { login } = useAuth();
  const router = useRouter();

  const onSubmit = async (data: LoginForm) => {
    try {
      const response = await apiClient.post("/auth/login", data);
      const { token } = response.data;
      login(token);
      toast.success("Zalogowano pomyślnie!");
    } catch (error: any) {
      if (error.response?.status === 401) {
        toast.error("Nieprawidłowy email lub hasło");
      } else {
        toast.error("Wystąpił błąd logowania");
      }
    }
  };

  return (
    <div className="flex min-h-[80vh] items-center justify-center">
      <div className="w-full max-w-md space-y-8 rounded-lg border bg-white p-8 shadow-lg">
        <div className="text-center">
          <h2 className="text-3xl font-bold tracking-tight text-gray-900">
            Zaloguj się
          </h2>
          <p className="mt-2 text-sm text-gray-600">
            Witaj ponownie w SwiftRent
          </p>
        </div>

        <form className="mt-8 space-y-6" onSubmit={handleSubmit(onSubmit)}>
          <div className="space-y-4">
            <Input
              label="Email"
              type="email"
              autoComplete="email"
              placeholder="admin@swiftrent.pl"
              {...register("email", {
                required: "Email jest wymagany",
                pattern: {
                  value: /^\S+@\S+$/i,
                  message: "Nieprawidłowy format email",
                },
              })}
              error={errors.email?.message}
            />

            <Input
              label="Hasło"
              type="password"
              autoComplete="current-password"
              placeholder="******"
              {...register("password", { required: "Hasło jest wymagane" })}
              error={errors.password?.message}
            />
          </div>

          <Button type="submit" className="w-full" disabled={isSubmitting}>
            {isSubmitting ? "Logowanie..." : "Zaloguj się"}
          </Button>

          <p className="text-center text-sm text-gray-600">
            Nie masz konta?{" "}
            <Link
              href="/register"
              className="font-medium text-emerald-600 hover:text-emerald-500"
            >
              Zarejestruj się
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}
