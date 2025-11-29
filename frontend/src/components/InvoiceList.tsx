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
import { Invoice, downloadInvoicePdf } from "@/services/api";
import { useMemo, useState, useCallback } from "react";
import { useParams } from "react-router-dom";

interface InvoiceListProps {
  invoices: Invoice[];
}

interface ColumnConfig {
  key: keyof Invoice;
  label: string;
  align?: "left" | "right" | "center";
  render?: (value: Invoice[keyof Invoice], invoice: Invoice) => React.ReactNode;
  hide?: boolean;
}

const InvoiceList = ({ invoices }: InvoiceListProps) => {
  const { userId } = useParams<{ userId: string }>();
  const [downloadingIds, setDownloadingIds] = useState<Set<number>>(new Set());

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

  const handleDownload = useCallback(
    async (invoiceId: number) => {
      if (!userId) return;

      setDownloadingIds((prev) => {
        // Prevent multiple clicks
        if (prev.has(invoiceId)) return prev;
        return new Set(prev).add(invoiceId);
      });

      try {
        await downloadInvoicePdf(parseInt(userId), invoiceId);
      } catch (error) {
        console.error("Failed to download invoice PDF:", error);
        alert("Failed to download invoice PDF. Please try again.");
      } finally {
        setDownloadingIds((prev) => {
          const newSet = new Set(prev);
          newSet.delete(invoiceId);
          return newSet;
        });
      }
    },
    [userId]
  );

  // Define columns in the exact order specified: id, amount, created_at, date_from, date_to, description, updated_at, action
  const columns = useMemo<ColumnConfig[]>(() => {
    if (!invoices || invoices.length === 0) {
      return [];
    }

    // Define column configurations in the exact order requested
    const columnOrder: Array<{ key: keyof Invoice; config: ColumnConfig }> = [
      {
        key: "id",
        config: {
          key: "id",
          label: "id",
          render: (value) => <span className="font-medium">#{value}</span>,
        },
      },
      {
        key: "amount",
        config: {
          key: "amount",
          label: "amount",
          align: "right",
          render: (value) => (
            <span className="font-semibold text-primary">
              {formatAmount(value as number)}
            </span>
          ),
        },
      },
      {
        key: "createdAt",
        config: {
          key: "createdAt",
          label: "created_at",
          render: (value) => (
            <span className="text-sm text-muted-foreground">
              {formatDate(value as string)}
            </span>
          ),
        },
      },
      {
        key: "dateFrom",
        config: {
          key: "dateFrom",
          label: "date_from",
          render: (value) => (
            <span className="text-sm">{formatDate(value as string)}</span>
          ),
        },
      },
      {
        key: "dateTo",
        config: {
          key: "dateTo",
          label: "date_to",
          render: (value) => (
            <span className="text-sm">{formatDate(value as string)}</span>
          ),
        },
      },
      {
        key: "description",
        config: {
          key: "description",
          label: "description",
          render: (value) => (
            <span className="text-sm line-clamp-2 max-w-xs">{value}</span>
          ),
        },
      },
      {
        key: "updatedAt",
        config: {
          key: "updatedAt",
          label: "updated_at",
          render: (value) => (
            <span className="text-sm text-muted-foreground">
              {formatDate(value as string)}
            </span>
          ),
        },
      },
      {
        key: "pdfUrl",
        config: {
          key: "pdfUrl",
          label: "action",
          align: "right",
          render: (value, invoice) => (
            <Button
              size="sm"
              variant="outline"
              onClick={() => handleDownload(invoice.id)}
              disabled={downloadingIds.has(invoice.id)}
              className="gap-2"
            >
              <Download className="h-4 w-4" />
              {downloadingIds.has(invoice.id) ? "Downloading..." : "PDF"}
            </Button>
          ),
        },
      },
    ];

    // Return columns in the specified order, only including fields that exist in the invoice
    return columnOrder
      .filter(({ key }) => key in (invoices[0] || {}))
      .map(({ config }) => config);
  }, [invoices, handleDownload, downloadingIds]);

  return (
    <div className="rounded-lg border border-border bg-card shadow-elegant overflow-hidden">
      <Table>
        <TableHeader>
          <TableRow className="border-b transition-colors data-[state=selected]:bg-muted hover:bg-muted/50 bg-muted/50">
            {columns.map((column) => (
              <TableHead
                key={column.key}
                className={`font-semibold ${
                  column.align === "right"
                    ? "text-right"
                    : column.align === "center"
                    ? "text-center"
                    : ""
                }`}
              >
                {column.label}
              </TableHead>
            ))}
          </TableRow>
        </TableHeader>
        <TableBody>
          {invoices.map((invoice) => (
            <TableRow
              key={invoice.id}
              className="hover:bg-muted/30 transition-colors"
            >
              {columns.map((column) => (
                <TableCell
                  key={column.key}
                  className={
                    column.align === "right"
                      ? "text-right"
                      : column.align === "center"
                      ? "text-center"
                      : ""
                  }
                >
                  {column.render
                    ? column.render(invoice[column.key], invoice)
                    : String(invoice[column.key] ?? "")}
                </TableCell>
              ))}
            </TableRow>
          ))}
        </TableBody>
      </Table>
    </div>
  );
};

export default InvoiceList;
