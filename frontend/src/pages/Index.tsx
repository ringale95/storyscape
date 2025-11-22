import { Link } from "react-router-dom";
import { Button } from "@/components/ui/button";
import { BookOpen } from "lucide-react";

const Index = () => {
  return (
    <div className="flex min-h-screen items-center justify-center bg-gradient-subtle">
      <div className="text-center space-y-6 animate-fade-in px-4">
        <div className="inline-flex items-center gap-3 mb-4">
          <div className="p-4 bg-gradient-primary rounded-2xl">
            <BookOpen className="w-10 h-10 text-primary-foreground" />
          </div>
        </div>
        <h1 className="text-5xl md:text-6xl font-bold text-foreground">
          Welcome to StoryScape
        </h1>
        <p className="text-xl text-muted-foreground max-w-2xl mx-auto">
          Where stories come to life. Begin your creative journey today.
        </p>
        <div className="flex gap-4 justify-center pt-4">
          <Button
            asChild
            size="lg"
            className="bg-gradient-primary hover:opacity-90 transition-opacity shadow-soft"
          >
            <Link to="/register">Get Started</Link>
          </Button>
          <Button
            asChild
            variant="outline"
            size="lg"
            className="hover:bg-secondary"
          >
            <Link to="/login">Sign In</Link>
          </Button>
        </div>
      </div>
    </div>
  );
};

export default Index;
