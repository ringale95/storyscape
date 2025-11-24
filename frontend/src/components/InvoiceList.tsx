import { Download, ExternalLink } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Table,
  TableBody,
  TableCell,
  TableHead,
  TableHeader,
  TableRow,
} from "@/components/ui/table";
import { Badge } from "@/components/ui/badge";
import { Invoice } from "@/pages/Invoices";

interface InvoiceListProps {
  invoices: Invoice[];
}

const InvoiceList = ({ invoices }: InvoiceListProps) => {
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

  const handleDownload = (pdfUrl: string, invoiceId: number) => {
    if (pdfUrl && pdfUrl !== "#") {
      window.open(pdfUrl, "_blank");
    }
  };

  return (
    <div className="rounded-lg border border-border bg-card shadow-elegant overflow-hidden">
      <Table>
        <TableHeader>
          <TableRow className="bg-muted/50">
            <TableHead className="font-semibold">Invoice ID</TableHead>
            <TableHead className="font-semibold">Billing Period</TableHead>
            <TableHead className="font-semibold">Subscription</TableHead>
            <TableHead className="font-semibold">Description</TableHead>
            <TableHead className="font-semibold text-right">Amount</TableHead>
            <TableHead className="font-semibold">Created On</TableHead>
            <TableHead className="font-semibold text-right">Action</TableHead>
          </TableRow>
        </TableHeader>
        <TableBody>
          {invoices.map((invoice) => (
            <TableRow key={invoice.id} className="hover:bg-muted/30 transition-colors">
              <TableCell className="font-medium">#{invoice.id}</TableCell>
              <TableCell>
                <div className="flex flex-col gap-1">
                  <span className="text-sm">{formatDate(invoice.dateFrom)}</span>
                  <span className="text-xs text-muted-foreground flex items-center gap-1">
                    <ExternalLink className="h-3 w-3" />
                    {formatDate(invoice.dateTo)}
                  </span>
                </div>
              </TableCell>
              <TableCell>
                <Badge variant="secondary" className="font-mono">
                  #{invoice.subscriptionId}
                </Badge>
              </TableCell>
              <TableCell className="max-w-xs">
                <span className="text-sm line-clamp-2">{invoice.description}</span>
              </TableCell>
              <TableCell className="text-right font-semibold text-primary">
                {formatAmount(invoice.amount)}
              </TableCell>
              <TableCell className="text-sm text-muted-foreground">
                {formatDate(invoice.createdAt)}
              </TableCell>
              <TableCell className="text-right">
                <Button
                  size="sm"
                  variant="outline"
                  onClick={() => handleDownload(invoice.pdfUrl, invoice.id)}
                  disabled={!invoice.pdfUrl || invoice.pdfUrl === "#"}
                  className="gap-2"
                >
                  <Download className="h-4 w-4" />
                  PDF
                </Button>
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
};

export default InvoiceList;
