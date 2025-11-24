const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

const getAuthHeaders = () => {
  const token = localStorage.getItem("jwt_token");
  return {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  };
};

export interface UserProfile {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  tier: "NORMAL" | "CORE" | "PRO";
  walletCents: number;
  walletDollars: number;
}

export const getUserProfile = async (id: number): Promise<UserProfile> => {
  const response = await fetch(`${API_BASE_URL}/users/${id}`, {
    method: "GET",
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || `Failed to fetch user profile: ${response.status}`);
  }

  return response.json();
};

export const topUpWallet = async (
  id: number, 
  amount: number, 
  currentWalletCents: number
): Promise<UserProfile> => {
  // Calculate new wallet balance (convert dollars to cents and add to current)
  const amountInCents = Math.round(amount * 100);
  const newWalletCents = currentWalletCents + amountInCents;

  const response = await fetch(`${API_BASE_URL}/users/${id}`, {
    method: "PATCH",
    headers: getAuthHeaders(),
    body: JSON.stringify({
      walletCents: newWalletCents,
    }),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || `Failed to top up wallet: ${response.status}`);
  }

  return response.json();
};
