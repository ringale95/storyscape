import { useState, useEffect } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardDescription,
} from "@/components/ui/card";
import { Input } from "@/components/ui/input";
import { Button } from "@/components/ui/button";
import { Badge } from "@/components/ui/badge";
import { Skeleton } from "@/components/ui/skeleton";
import { toast } from "@/hooks/use-toast";
import {
  User,
  Wallet,
  Mail,
  Hash,
  CreditCard,
  FileText,
  ArrowRight,
} from "lucide-react";
import { getUserProfile, topUpWallet } from "@/services/api";

interface UserProfile {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  tier: "NORMAL" | "CORE" | "PRO";
  walletCents: number;
  walletDollars: number;
}

const Profile = () => {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [profile, setProfile] = useState<UserProfile | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [topUpAmount, setTopUpAmount] = useState<string>("");
  const [isTopping, setIsTopping] = useState(false);

  useEffect(() => {
    const fetchProfile = async () => {
      if (!id) return;

      try {
        setLoading(true);
        const data = await getUserProfile(parseInt(id));
        setProfile(data);
        setError(null);
      } catch (err) {
        setError(err.message || "Failed to load profile");
        toast({
          title: "Error",
          description: err.message || "Failed to load profile",
          variant: "destructive",
        });
      } finally {
        setLoading(false);
      }
    };

    fetchProfile();
  }, [id]);

  const handleTopUp = async () => {
    if (!id || !topUpAmount || parseFloat(topUpAmount) <= 0) {
      toast({
        title: "Invalid Amount",
        description: "Please enter a valid amount",
        variant: "destructive",
      });
      return;
    }

    if (!profile) {
      toast({
        title: "Error",
        description: "Profile not loaded",
        variant: "destructive",
      });
      return;
    }

    try {
      setIsTopping(true);
      const updatedProfile = await topUpWallet(
        parseInt(id),
        parseFloat(topUpAmount),
        profile.walletCents
      );
      setProfile(updatedProfile);
      setTopUpAmount("");
      toast({
        title: "Success",
        description: `Wallet topped up with $${topUpAmount}`,
      });
    } catch (err) {
      toast({
        title: "Top-Up Failed",
        description: err.message || "Failed to top up wallet",
        variant: "destructive",
      });
    } finally {
      setIsTopping(false);
    }
  };

  const getTierColor = (tier: string) => {
    switch (tier) {
      case "PRO":
        return "bg-gradient-primary text-primary-foreground";
      case "CORE":
        return "bg-secondary text-secondary-foreground";
      default:
        return "bg-muted text-muted-foreground";
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background to-background-secondary">
        <div className="container mx-auto px-4 py-8 max-w-4xl">
          <Card className="shadow-elegant">
            <CardHeader>
              <Skeleton className="h-8 w-48" />
              <Skeleton className="h-4 w-64 mt-2" />
            </CardHeader>
            <CardContent className="space-y-6">
              <Skeleton className="h-24 w-full" />
              <Skeleton className="h-32 w-full" />
            </CardContent>
          </Card>
        </div>
      </div>
    );
  }

  if (error || !profile) {
    return (
      <div className="min-h-screen bg-gradient-to-br from-background to-background-secondary flex items-center justify-center">
        <Card className="max-w-md shadow-elegant">
          <CardHeader>
            <CardTitle className="text-destructive">Error</CardTitle>
            <CardDescription>{error || "Profile not found"}</CardDescription>
          </CardHeader>
          <CardContent>
            <Button onClick={() => navigate("/")}>Go Home</Button>
          </CardContent>
        </Card>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gradient-to-br from-background to-background-secondary">
      <div className="container mx-auto px-4 py-8 max-w-4xl">
        <div className="space-y-6 animate-fade-in">
          {/* User Details Card */}
          <Card className="shadow-elegant">
            <CardHeader>
              <div className="flex items-center justify-between">
                <div>
                  <CardTitle className="text-3xl">
                    {profile.firstName} {profile.lastName}
                  </CardTitle>
                  <CardDescription className="mt-2">
                    User Profile
                  </CardDescription>
                </div>
                <Badge
                  className={getTierColor(profile.tier)}
                  variant="secondary"
                >
                  {profile.tier}
                </Badge>
              </div>
            </CardHeader>
            <CardContent className="space-y-4">
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                <div className="flex items-center gap-3 p-4 rounded-lg bg-muted/50">
                  <User className="h-5 w-5 text-muted-foreground" />
                  <div>
                    <p className="text-sm text-muted-foreground">Full Name</p>
                    <p className="font-medium">
                      {profile.firstName} {profile.lastName}
                    </p>
                  </div>
                </div>

                <div className="flex items-center gap-3 p-4 rounded-lg bg-muted/50">
                  <Mail className="h-5 w-5 text-muted-foreground" />
                  <div>
                    <p className="text-sm text-muted-foreground">Email</p>
                    <p className="font-medium break-all">{profile.email}</p>
                  </div>
                </div>

                <div className="flex items-center gap-3 p-4 rounded-lg bg-muted/50">
                  <Hash className="h-5 w-5 text-muted-foreground" />
                  <div>
                    <p className="text-sm text-muted-foreground">User ID</p>
                    <p className="font-medium">{profile.id}</p>
                  </div>
                </div>

                <div className="flex items-center gap-3 p-4 rounded-lg bg-muted/50">
                  <CreditCard className="h-5 w-5 text-muted-foreground" />
                  <div>
                    <p className="text-sm text-muted-foreground">Tier</p>
                    <p className="font-medium">{profile.tier}</p>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>

          {/* Wallet Card */}
          <Card className="shadow-elegant">
            <CardHeader>
              <div className="flex items-center gap-2">
                <Wallet className="h-6 w-6 text-primary" />
                <CardTitle>Wallet Balance</CardTitle>
              </div>
              <CardDescription>
                Manage your wallet and top up balance
              </CardDescription>
            </CardHeader>
            <CardContent className="space-y-6">
              {/* Balance Display */}
              <div className="p-6 rounded-lg bg-gradient-subtle border border-border">
                <div className="flex items-baseline justify-between">
                  <div>
                    <p className="text-sm text-muted-foreground mb-1">
                      Current Balance
                    </p>
                    <p className="text-4xl font-bold text-foreground">
                      ${profile.walletDollars.toFixed(2)}
                    </p>
                    <p className="text-sm text-muted-foreground mt-1">
                      {profile.walletCents.toLocaleString()} cents
                    </p>
                  </div>
                </div>
              </div>

              {/* Top-Up Section */}
              <div className="space-y-4">
                <div>
                  <label
                    htmlFor="topup-amount"
                    className="text-sm font-medium mb-2 block"
                  >
                    Top-Up Amount (USD)
                  </label>
                  <Input
                    id="topup-amount"
                    type="number"
                    min="0"
                    step="0.01"
                    placeholder="Enter amount"
                    value={topUpAmount}
                    onChange={(e) => setTopUpAmount(e.target.value)}
                    className="text-lg"
                  />
                </div>

                <Button
                  onClick={handleTopUp}
                  disabled={
                    isTopping || !topUpAmount || parseFloat(topUpAmount) <= 0
                  }
                  className="w-full"
                  size="lg"
                >
                  {isTopping ? "Processing..." : "Top Up Wallet"}
                </Button>
              </div>
            </CardContent>
          </Card>

          {/* Invoices Card */}
          <Card className="shadow-elegant">
            <CardHeader>
              <div className="flex items-center gap-2">
                <FileText className="h-6 w-6 text-primary" />
                <CardTitle>Invoices</CardTitle>
              </div>
              <CardDescription>
                View your billing history and invoices
              </CardDescription>
            </CardHeader>
            <CardContent>
              <Link to={`/users/${profile.id}/invoices`}>
                <Button variant="outline" className="w-full group" size="lg">
                  <span className="flex items-center justify-between w-full">
                    <span>View All Invoices</span>
                    <ArrowRight className="h-4 w-4 transition-transform group-hover:translate-x-1" />
                  </span>
                </Button>
              </Link>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default Profile;
