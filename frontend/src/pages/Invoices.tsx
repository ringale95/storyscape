import { useState } from "react";
import { useParams } from "react-router-dom";
import { useQuery } from "@tanstack/react-query";
import InvoiceList from "@/components/InvoiceList";
import InvoiceCard from "@/components/InvoiceCard";
import LoadingSkeleton from "@/components/LoadingSkeleton";
import EmptyState from "@/components/EmptyState";
import { Input } from "@/components/ui/input";
import { Search } from "lucide-react";

export interface Invoice {
  id: number;
  subscriptionId: number;
  dateFrom: string;
  dateTo: string;
  amount: number;
  description: string;
  createdAt: string;
  pdfUrl: string;
}

// Mock API call - replace with actual API
const fetchInvoices = async (userId: string): Promise<Invoice[]> => {
  // Simulate API delay
  await new Promise(resolve => setTimeout(resolve, 1000));
  
  // Mock data
  return [
    {
      id: 1001,
      subscriptionId: 5421,
      dateFrom: "2025-01-01",
      dateTo: "2025-01-31",
      amount: 49.99,
      description: "Premium Featured Post - January 2025",
      createdAt: "2025-02-01",
      pdfUrl: "#"
    },
    {
      id: 1002,
      subscriptionId: 5422,
      dateFrom: "2025-02-01",
      dateTo: "2025-02-28",
      amount: 99.99,
      description: "Analytics Pack Pro - February 2025",
      createdAt: "2025-03-01",
      pdfUrl: "#"
    },
    {
      id: 1003,
      subscriptionId: 5421,
      dateFrom: "2025-02-01",
      dateTo: "2025-02-28",
      amount: 49.99,
      description: "Premium Featured Post - February 2025",
      createdAt: "2025-03-01",
      pdfUrl: "#"
    }
  ];
};

const Invoices = () => {
  const { userId } = useParams<{ userId: string }>();
  const [searchTerm, setSearchTerm] = useState("");

  const { data: invoices, isLoading, error } = useQuery({
    queryKey: ["invoices", userId],
    queryFn: () => fetchInvoices(userId!),
    enabled: !!userId,
  });

  const filteredInvoices = invoices?.filter(invoice => 
    invoice.description.toLowerCase().includes(searchTerm.toLowerCase()) ||
    invoice.subscriptionId.toString().includes(searchTerm) ||
    invoice.id.toString().includes(searchTerm)
  );

  if (isLoading) {
    return <LoadingSkeleton />;
  }

  if (error) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background to-background-secondary flex items-center justify-center p-6">
        <div className="text-center">
          <h2 className="text-2xl font-bold text-destructive mb-2">Error Loading Invoices</h2>
          <p className="text-muted-foreground">Please try again later.</p>
        </div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background to-background-secondary">
      <div className="container mx-auto px-4 py-8 max-w-7xl">
        {/* Header Section */}
        <div className="mb-8 animate-fade-in">
          <h1 className="text-4xl md:text-5xl font-bold bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent mb-3">
            Your Invoices
          </h1>
          <p className="text-muted-foreground text-lg">
            View and download your monthly billing invoices.
          </p>
        </div>

        {/* Search Bar */}
        {invoices && invoices.length > 0 && (
          <div className="mb-6 animate-slide-in">
            <div className="relative max-w-md">
              <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 text-muted-foreground h-4 w-4" />
              <Input
                type="text"
                placeholder="Search by invoice ID, subscription, or description..."
                value={searchTerm}
                onChange={(e) => setSearchTerm(e.target.value)}
                className="pl-10"
              />
            </div>
          </div>
        )}

        {/* Invoices Display */}
        {!filteredInvoices || filteredInvoices.length === 0 ? (
          <EmptyState />
        ) : (
          <>
            {/* Desktop Table View */}
            <div className="hidden md:block animate-slide-in">
              <InvoiceList invoices={filteredInvoices} />
            </div>

            {/* Mobile Card View */}
            <div className="md:hidden space-y-4 animate-slide-in">
              {filteredInvoices.map((invoice) => (
                <InvoiceCard key={invoice.id} invoice={invoice} />
              ))}
            </div>
          </>
        )}
      </div>
    </div>
  );
};

export default Invoices;
