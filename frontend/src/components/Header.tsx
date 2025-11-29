import { useState } from "react";
import { Link, useNavigate } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { NavLink } from "@/components/NavLink";
import { Menu, X, BookOpen, LogOut, Crown } from "lucide-react";
import { useToast } from "@/hooks/use-toast";

interface HeaderProps {
  userId?: number;
}

const Header = ({ userId = 2 }: HeaderProps) => {
  const [mobileMenuOpen, setMobileMenuOpen] = useState(false);
  const navigate = useNavigate();
  const { toast } = useToast();

  const handleLogout = () => {
    localStorage.removeItem("jwt_token");
    toast({
      title: "Logged out successfully",
      description: "You have been logged out of your account.",
    });
    navigate("/login");
  };

  const navLinks = [
    { to: "/", label: "Home" },
    { to: `/users/${userId}`, label: "Profile" },
    { to: `/users/${userId}/invoices`, label: "Invoices" },
    { to: "/feed", label: "Feed" },
    { to: "/products", label: "Products" },
  ];

  return (
    <header className="sticky top-0 z-50 w-full border-b border-border bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60">
      <nav className="container mx-auto px-4 h-16 flex items-center justify-between">
        {/* Logo */}
        <Link to="/" className="flex items-center gap-2 group">
          <div className="p-2 bg-gradient-primary rounded-lg transition-transform group-hover:scale-105">
            <BookOpen className="w-5 h-5 text-primary-foreground" />
          </div>
          <span className="text-xl font-bold text-foreground">StoryScape</span>
        </Link>

        {/* Desktop Navigation */}
        <div className="hidden md:flex items-center gap-6">
          {navLinks.map((link) => (
            <NavLink
              key={link.to}
              to={link.to}
              className="text-sm font-medium text-muted-foreground hover:text-foreground transition-colors"
              activeClassName="text-foreground"
            >
              {link.label}
            </NavLink>
          ))}
          {/* Member only Content - Premium Styled */}
          <Link
            to="#"
            className="relative flex items-center gap-2 px-4 py-2 rounded-lg bg-gradient-to-r from-amber-500 via-yellow-500 to-amber-600 text-white font-semibold text-sm shadow-lg hover:shadow-xl transition-all duration-300 hover:scale-105 group"
            onClick={(e) => {
              e.preventDefault();
              toast({
                title: "Member Only Content",
                description: "This is exclusive content for members only!",
              });
            }}
          >
            <Crown className="w-4 h-4 group-hover:rotate-12 transition-transform" />
            <span>Member only Content</span>
            <div className="absolute inset-0 rounded-lg bg-gradient-to-r from-amber-400 to-yellow-400 opacity-0 group-hover:opacity-20 transition-opacity"></div>
          </Link>
          <Button
            variant="ghost"
            size="sm"
            onClick={handleLogout}
            className="gap-2"
          >
            <LogOut className="w-4 h-4" />
            Logout
          </Button>
        </div>

        {/* Mobile Menu Button */}
        <button
          className="md:hidden p-2 text-foreground"
          onClick={() => setMobileMenuOpen(!mobileMenuOpen)}
          aria-label="Toggle menu"
        >
          {mobileMenuOpen ? (
            <X className="w-6 h-6" />
          ) : (
            <Menu className="w-6 h-6" />
          )}
        </button>
      </nav>

      {/* Mobile Menu */}
      {mobileMenuOpen && (
        <div className="md:hidden border-t border-border bg-background animate-fade-in">
          <div className="container mx-auto px-4 py-4 space-y-3">
            {navLinks.map((link) => (
              <NavLink
                key={link.to}
                to={link.to}
                className="block py-2 text-sm font-medium text-muted-foreground hover:text-foreground transition-colors"
                activeClassName="text-foreground"
                onClick={() => setMobileMenuOpen(false)}
              >
                {link.label}
              </NavLink>
            ))}
            {/* Member only Content - Premium Styled (Mobile) */}
            <Link
              to="#"
              className="flex items-center gap-2 px-4 py-3 rounded-lg bg-gradient-to-r from-amber-500 via-yellow-500 to-amber-600 text-white font-semibold text-sm shadow-lg hover:shadow-xl transition-all duration-300 group"
              onClick={(e) => {
                e.preventDefault();
                setMobileMenuOpen(false);
                toast({
                  title: "Member Only Content",
                  description: "This is exclusive content for members only!",
                });
              }}
            >
              <Crown className="w-4 h-4 group-hover:rotate-12 transition-transform" />
              <span>Member only Content</span>
            </Link>
            <Button
              variant="ghost"
              size="sm"
              onClick={() => {
                handleLogout();
                setMobileMenuOpen(false);
              }}
              className="w-full justify-start gap-2"
            >
              <LogOut className="w-4 h-4" />
              Logout
            </Button>
          </div>
        </div>
      )}
    </header>
  );
};

export default Header;
