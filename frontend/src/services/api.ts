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

export interface Invoice {
  id: number;
  subscriptionId: number;
  dateFrom: string;
  dateTo: string;
  amount: number;
  description: string;
  createdAt: string;
  updatedAt: string;
  pdfUrl: string;
}

export const getInvoices = async (userId: number): Promise<Invoice[]> => {
  const response = await fetch(`${API_BASE_URL}/users/${userId}/invoices`, {
    method: "GET",
    headers: getAuthHeaders(),
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || `Failed to fetch invoices: ${response.status}`);
  }

  return response.json();
};

export const downloadInvoicePdf = async (userId: number, invoiceId: number): Promise<void> => {
  const token = localStorage.getItem("jwt_token");
  const headers: HeadersInit = {
    ...(token && { Authorization: `Bearer ${token}` }),
  };

  const response = await fetch(`${API_BASE_URL}/users/${userId}/invoices/${invoiceId}/pdf`, {
    method: "GET",
    headers: headers,
  });

  if (!response.ok) {
    const errorText = await response.text();
    throw new Error(errorText || `Failed to download invoice PDF: ${response.status}`);
  }

  // Get the PDF blob
  const blob = await response.blob();
  
  // Create a download link
  const url = window.URL.createObjectURL(blob);
  const link = document.createElement("a");
  link.href = url;
  
  // Get filename from Content-Disposition header or use default
  const contentDisposition = response.headers.get("Content-Disposition");
  let filename = `invoice_${invoiceId}.pdf`;
  if (contentDisposition) {
    const filenameMatch = contentDisposition.match(/filename="?(.+)"?/i);
    if (filenameMatch) {
      filename = filenameMatch[1];
    }
  }
  
  link.setAttribute("download", filename);
  document.body.appendChild(link);
  link.click();
  
  // Cleanup
  link.remove();
  window.URL.revokeObjectURL(url);
};
