import { FileText, Inbox } from "lucide-react";
import { Card, CardContent } from "@/components/ui/card";

const EmptyState = () => {
  return (
    <div className="flex items-center justify-center min-h-[400px]">
      <Card className="max-w-md w-full shadow-elegant">
        <CardContent className="pt-12 pb-12 text-center">
          <div className="mb-6 flex justify-center">
            <div className="relative">
              <div className="absolute inset-0 bg-primary/10 rounded-full blur-xl animate-pulse" />
              <div className="relative bg-gradient-to-br from-primary/20 to-accent/20 p-6 rounded-full">
                <Inbox className="h-16 w-16 text-primary" />
              </div>
            </div>
          </div>
          
          <h3 className="text-2xl font-bold mb-3 bg-gradient-to-r from-primary to-accent bg-clip-text text-transparent">
            No Invoices Yet
          </h3>
          
          <p className="text-muted-foreground mb-6 max-w-sm mx-auto">
            Your invoices will appear here once you subscribe to any of our services. 
            Each subscription generates a monthly invoice.
          </p>

          <div className="flex items-center justify-center gap-2 text-sm text-muted-foreground">
            <FileText className="h-4 w-4" />
            <span>Invoices are generated automatically</span>
          </div>
        </CardContent>
      </Card>
    </div>
  );
};

export default EmptyState;
