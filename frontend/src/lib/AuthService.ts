import { RegisterDTO } from "@/dto/RegisterDTO";

const TOKEN_KEY = "jwt_token";

export const AuthService = {
  async login(email: string, password: string): Promise<string> {
    const res = await fetch("http://localhost:8080/auth/login", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({ email, password }),
    });
    if (!res.ok) {
      const error = await res.json().catch(() => ({}));
      throw { response: { data: error } };
    }
    const token = await res.text();
    localStorage.setItem(TOKEN_KEY, token);
    return token;
  },

  async register(data: RegisterDTO): Promise<void> {
    const res = await fetch("http://localhost:8080/auth/register", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify({
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        password: data.password,
        bio: data.bio || "",
        tier: data.tier || "NORMAL",
      }),
    });

    if (!res.ok) {
      const error = await res.json().catch(() => ({ message: "Registration failed" }));
      throw new Error(error.message || "Registration failed");
    }
  },

  getToken(): string | null {
    return localStorage.getItem(TOKEN_KEY);
  },
  
  logout() {
    localStorage.removeItem(TOKEN_KEY);
  },
};
