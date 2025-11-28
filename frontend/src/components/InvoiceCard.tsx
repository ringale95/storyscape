import { Download, Calendar, DollarSign, FileText } from "lucide-react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader } from "@/components/ui/card";
import { Badge } from "@/components/ui/badge";
import { Invoice, downloadInvoicePdf } from "@/services/api";
import { useParams } from "react-router-dom";
import { useState } from "react";

interface InvoiceCardProps {
  invoice: Invoice;
}

const InvoiceCard = ({ invoice }: InvoiceCardProps) => {
  const { userId } = useParams<{ userId: string }>();
  const [isDownloading, setIsDownloading] = useState(false);

  const formatDate = (dateString: string) => {
    return new Date(dateString).toLocaleDateString("en-US", {
      year: "numeric",
      month: "short",
      day: "numeric",
    });
  };

  const formatAmount = (amount: number) => {
    return new Intl.NumberFormat("en-US", {
      style: "currency",
      currency: "USD",
    }).format(amount);
  };

  const handleDownload = async () => {
    if (!userId || isDownloading) return;

    try {
      setIsDownloading(true);
      await downloadInvoicePdf(parseInt(userId), invoice.id);
    } catch (error) {
      console.error("Failed to download invoice PDF:", error);
      alert("Failed to download invoice PDF. Please try again.");
    } finally {
      setIsDownloading(false);
    }
  };

  return (
    <Card className="hover:shadow-elegant transition-shadow">
      <CardHeader className="pb-3">
        <div className="flex items-start justify-between">
          <div>
            <h3 className="font-semibold text-lg">Invoice #{invoice.id}</h3>
            <Badge variant="secondary" className="mt-2 font-mono text-xs">
              Subscription #{invoice.subscriptionId}
            </Badge>
          </div>
          <Button
            size="sm"
            variant="outline"
            onClick={handleDownload}
            disabled={isDownloading}
            className="gap-2"
          >
            <Download className="h-4 w-4" />
            {isDownloading ? "Downloading..." : "PDF"}
          </Button>
        </div>
      </CardHeader>
      <CardContent className="space-y-3">
        <div className="flex items-start gap-2">
          <FileText className="h-4 w-4 text-muted-foreground mt-0.5" />
          <div>
            <p className="text-sm text-muted-foreground">Description</p>
            <p className="text-sm font-medium">{invoice.description}</p>
          </div>
        </div>

        <div className="flex items-start gap-2">
          <Calendar className="h-4 w-4 text-muted-foreground mt-0.5" />
          <div>
            <p className="text-sm text-muted-foreground">Billing Period</p>
            <p className="text-sm font-medium">
              {formatDate(invoice.dateFrom)} → {formatDate(invoice.dateTo)}
            </p>
          </div>
        </div>

        <div className="flex items-start gap-2">
          <DollarSign className="h-4 w-4 text-muted-foreground mt-0.5" />
          <div>
            <p className="text-sm text-muted-foreground">Amount</p>
            <p className="text-lg font-bold text-primary">
              {formatAmount(invoice.amount)}
            </p>
          </div>
        </div>

        <div className="pt-2 border-t border-border">
          <p className="text-xs text-muted-foreground">
            Created on {formatDate(invoice.createdAt)}
          </p>
        </div>
      </CardContent>
    </Card>
  );
};

export default InvoiceCard;
