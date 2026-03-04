"use client";

import { useState } from "react";
import Link from "next/link";
import { useAuth } from "@/context/AuthContext";
import { Button } from "./ui/Button";
import { Car, LogOut, User, Menu, X, Users, History } from "lucide-react";

export const Navbar = () => {
  const { user, isAuthenticated, logout, isAdmin } = useAuth();
  const [isMenuOpen, setIsMenuOpen] = useState(false);

  const toggleMenu = () => setIsMenuOpen(!isMenuOpen);
  const closeMenu = () => setIsMenuOpen(false);

  return (
    <nav className="border-b bg-white shadow-sm sticky top-0 z-50">
      <div className="container mx-auto px-4">
        <div className="flex h-16 items-center justify-between">
          <Link
            href="/"
            onClick={closeMenu}
            className="flex items-center gap-2 text-xl font-bold text-emerald-600"
          >
            <Car className="h-6 w-6" />
            SwiftRent
          </Link>

          {/* Desktop Navigation */}
          <div className="hidden lg:flex items-center gap-4">
            {isAuthenticated ? (
              <>
                <Link href="/dashboard">
                  <Button variant="ghost" size="sm" className="gap-2">
                    <User className="h-4 w-4" />
                    Panel
                  </Button>
                </Link>
                {isAdmin && (
                  <>
                    <Link href="/admin/users">
                      <Button variant="ghost" size="sm" className="gap-2">
                        <Users className="h-4 w-4" />
                        Użytkownicy
                      </Button>
                    </Link>
                    <Link href="/admin/rentals">
                      <Button variant="ghost" size="sm" className="gap-2">
                        <History className="h-4 w-4" />
                        Wypożyczenia
                      </Button>
                    </Link>
                    <span className="rounded bg-emerald-100 px-2 py-1 text-xs font-semibold text-emerald-800">
                      ADMIN
                    </span>
                  </>
                )}
                <div className="text-sm text-gray-600 border-l pl-4 ml-2">
                  {user?.sub}
                </div>
                <Button
                  variant="outline"
                  size="sm"
                  onClick={logout}
                  className="gap-2 text-gray-700 hover:text-gray-900 border-gray-300"
                >
                  <LogOut className="h-4 w-4" />
                  Wyloguj
                </Button>
              </>
            ) : (
              <>
                <Link href="/login">
                  <Button variant="ghost" size="sm">
                    Logowanie
                  </Button>
                </Link>
                <Link href="/register">
                  <Button variant="primary" size="sm">
                    Rejestracja
                  </Button>
                </Link>
              </>
            )}
          </div>

          {/* Mobile Menu Button */}
          <div className="lg:hidden">
            <Button
              variant="ghost"
              size="sm"
              onClick={toggleMenu}
              className="p-2"
            >
              {isMenuOpen ? <X size={24} /> : <Menu size={24} />}
            </Button>
          </div>
        </div>

        {/* Mobile Navigation */}
        {isMenuOpen && (
          <div className="lg:hidden pb-4 pt-2 border-t space-y-2">
            {isAuthenticated ? (
              <>
                <div className="px-2 py-2 text-sm font-medium text-gray-500 border-b mb-2">
                  Zalogowany jako:{" "}
                  <span className="text-gray-900">{user?.sub}</span>
                  {isAdmin && (
                    <span className="ml-2 rounded bg-emerald-100 px-2 py-0.5 text-xs font-semibold text-emerald-800">
                      ADMIN
                    </span>
                  )}
                </div>
                <Link href="/dashboard" onClick={closeMenu} className="block">
                  <Button
                    variant="ghost"
                    className="w-full justify-start gap-3"
                  >
                    <User size={18} /> Panel użytkownika
                  </Button>
                </Link>
                {isAdmin && (
                  <>
                    <Link
                      href="/admin/users"
                      onClick={closeMenu}
                      className="block"
                    >
                      <Button
                        variant="ghost"
                        className="w-full justify-start gap-3"
                      >
                        <Users size={18} /> Zarządzanie użytkownikami
                      </Button>
                    </Link>
                    <Link
                      href="/admin/rentals"
                      onClick={closeMenu}
                      className="block"
                    >
                      <Button
                        variant="ghost"
                        className="w-full justify-start gap-3"
                      >
                        <History size={18} /> Zarządzanie wypożyczeniami
                      </Button>
                    </Link>
                  </>
                )}
                <div className="pt-2 border-t">
                  <Button
                    variant="outline"
                    onClick={() => {
                      logout();
                      closeMenu();
                    }}
                    className="w-full justify-start gap-3 text-red-600 hover:bg-red-50 hover:text-red-700 border-red-200"
                  >
                    <LogOut size={18} /> Wyloguj się
                  </Button>
                </div>
              </>
            ) : (
              <div className="grid grid-cols-2 gap-2">
                <Link href="/login" onClick={closeMenu}>
                  <Button variant="ghost" className="w-full">
                    Logowanie
                  </Button>
                </Link>
                <Link href="/register" onClick={closeMenu}>
                  <Button variant="primary" className="w-full">
                    Rejestracja
                  </Button>
                </Link>
              </div>
            )}
          </div>
        )}
      </div>
    </nav>
  );
};
