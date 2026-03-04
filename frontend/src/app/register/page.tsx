"use client";

import { useForm } from "react-hook-form";
import apiClient from "@/lib/apiClient";
import { useAuth } from "@/context/AuthContext";
import { Button } from "@/components/ui/Button";
import { Input } from "@/components/ui/Input";
import toast from "react-hot-toast";
import { useRouter } from "next/navigation";
import Link from "next/link";

interface RegisterForm {
  email: string;
  password: string;
  confirmPassword?: string;
}

export default function RegisterPage() {
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors, isSubmitting },
  } = useForm<RegisterForm>();
  const { login } = useAuth();
  const router = useRouter();

  const onSubmit = async (data: RegisterForm) => {
    try {
      const response = await apiClient.post("/auth/register", {
        email: data.email,
        password: data.password,
      });
      const { token } = response.data;
      if (token) {
        login(token);
        toast.success("Konto zostało utworzone!");
        router.push("/");
      } else {
        toast.success("Konto zostało utworzone! Zaloguj się.");
        router.push("/login");
      }
    } catch (error: any) {
      if (error.response?.status === 409) {
        toast.error("Użytkownik z tym emailem już istnieje");
      } else {
        toast.error("Wystąpił błąd podczas rejestracji");
      }
    }
  };

  const password = watch("password");

  return (
    <div className="flex min-h-[80vh] items-center justify-center">
      <div className="w-full max-w-md space-y-8 rounded-lg border bg-white p-8 shadow-lg">
        <div className="text-center">
          <h2 className="text-3xl font-bold tracking-tight text-gray-900">
            Utwórz konto
          </h2>
          <p className="mt-2 text-sm text-gray-600">
            Dołącz do SwiftRent i zacznij jeździć
          </p>
        </div>

        <form className="mt-8 space-y-6" onSubmit={handleSubmit(onSubmit)}>
          <div className="space-y-4">
            <Input
              label="Email"
              type="email"
              placeholder="jan@kowalski.pl"
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
              placeholder="Minimum 6 znaków"
              {...register("password", {
                required: "Hasło jest wymagane",
                minLength: {
                  value: 6,
                  message: "Hasło musi mieć minimum 6 znaków",
                },
              })}
              error={errors.password?.message}
            />

            <Input
              label="Potwierdź hasło"
              type="password"
              placeholder="Powtórz hasło"
              {...register("confirmPassword", {
                required: "Potwierdzenie hasła jest wymagane",
                validate: (value) =>
                  value === password || "Hasła nie są identyczne",
              })}
              error={errors.confirmPassword?.message}
            />
          </div>

          <Button type="submit" className="w-full" disabled={isSubmitting}>
            {isSubmitting ? "Rejestrowanie..." : "Zarejestruj się"}
          </Button>

          <p className="text-center text-sm text-gray-600">
            Masz już konto?{" "}
            <Link
              href="/login"
              className="font-medium text-emerald-600 hover:text-emerald-500"
            >
              Zaloguj się
            </Link>
          </p>
        </form>
      </div>
    </div>
  );
}
