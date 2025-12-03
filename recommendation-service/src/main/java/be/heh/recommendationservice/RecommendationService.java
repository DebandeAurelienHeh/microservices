package be.heh.recommendationservice;

import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import java.util.List;

@Service
public class RecommendationService {

    private final List<Recommendation> hardcodedRecommendations = List.of(
            new Recommendation(1, 1, "Marie Dupont", 5, "Excellent appareil photo, la qualité des photos est exceptionnelle!"),
            new Recommendation(1, 2, "Thomas Lambert", 4, "Bon rapport qualité-prix, mais l'autonomie pourrait être meilleure"),
            new Recommendation(2, 3, "Sophie Martin", 5, "Ces écouteurs sans fil sont parfaits pour le sport"),
            new Recommendation(2, 4, "Lucas Dubois", 4, "Bonne qualité sonore, confortable pour une utilisation prolongée"),
            new Recommendation(3, 5, "Emma Bernard", 5, "Cette montre connectée dépasse mes attentes"),
            new Recommendation(3, 6, "Pierre Leroy", 3, "Design élégant mais l'application pourrait être plus intuitive"),
            new Recommendation(4, 7, "Julie Moreau", 5, "Smartphone très performant, l'appareil photo est incroyable"),
            new Recommendation(4, 8, "Alexandre Garcia", 4, "Interface fluide, seul bémol : la batterie se décharge un peu vite"),
            new Recommendation(5, 9, "Camille Petit", 5, "Tablette parfaite pour le travail et les loisirs"),
            new Recommendation(5, 10, "Nicolas Roux", 4, "Très satisfait de cet achat, idéal pour la lecture et le streaming")
    );

    public Flux<Recommendation> getRecommendations(int productId) {
        return Flux.fromIterable(hardcodedRecommendations)
                .filter(recommendation -> recommendation.productId() == productId);
    }
}