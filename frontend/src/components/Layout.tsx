import { ReactNode } from "react";
import Header from "./Header";
import Footer from "./Footer";

interface LayoutProps {
  children: ReactNode;
  userId?: number;
  showHeader?: boolean;
  showFooter?: boolean;
}

const Layout = ({ 
  children, 
  userId, 
  showHeader = true, 
  showFooter = true 
}: LayoutProps) => {
  return (
    <div className="min-h-screen flex flex-col">
      {showHeader && <Header userId={userId} />}
      <main className="flex-1">{children}</main>
      {showFooter && <Footer />}
    </div>
  );
};

export default Layout;
