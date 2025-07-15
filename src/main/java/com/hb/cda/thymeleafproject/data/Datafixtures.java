package com.hb.cda.thymeleafproject.data;

import com.hb.cda.thymeleafproject.entity.Product;
import com.hb.cda.thymeleafproject.entity.User;
import com.hb.cda.thymeleafproject.repository.ProductRepository;
import com.hb.cda.thymeleafproject.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Datafixtures implements CommandLineRunner {

    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PasswordEncoder passwordEncoder;

    public Datafixtures(UserRepository userRepository, ProductRepository productRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.productRepository = productRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            User admin = new User("admin", passwordEncoder.encode("adminpass"), "ROLE_ADMIN");
            User user = new User("user", passwordEncoder.encode("userpass"), "ROLE_USER");

            userRepository.saveAll(List.of(admin, user));
            System.out.println(" Utilisateurs ajoutés avec encodage");
        }

        if (productRepository.count() == 0) {
            List<Product> products = List.of(
                    new Product("Café moulu", 4.99, "Paquet de 250g de café 100% arabica", 150),
                    new Product("Thé vert", 3.49, "Sachet de 100g de thé vert bio", 120),
                    new Product("Chocolat noir", 2.99, "Tablette de chocolat noir 70%", 90),
                    new Product("Bouteille d'eau", 0.99, "Bouteille 1L eau minérale", 500),
                    new Product("Brosse à dents", 1.50, "Brosse à dents souple", 200),
                    new Product("Dentifrice", 2.30, "Tube de dentifrice 75ml", 180),
                    new Product("Savon liquide", 3.10, "Flacon de 500ml au citron", 80),
                    new Product("Shampoing", 4.50, "Shampoing cheveux normaux 250ml", 130),
                    new Product("Crème hydratante", 7.99, "Crème visage pour peau sensible", 75),
                    new Product("Lait demi-écrémé", 1.10, "Brique de 1L", 300),
                    new Product("Jus d'orange", 2.20, "Bouteille 1L pur jus", 150),
                    new Product("Farine de blé", 1.40, "Paquet de 1kg T45", 100),
                    new Product("Pâtes", 1.20, "Paquet de 500g spaghetti", 250),
                    new Product("Riz basmati", 2.00, "Sachet de 1kg", 210),
                    new Product("Huile d'olive", 5.50, "Bouteille de 750ml", 60),
                    new Product("Miel", 4.20, "Pot de 250g de miel bio", 95),
                    new Product("Confiture de fraise", 3.80, "Pot de 350g", 110),
                    new Product("Sel de mer", 1.00, "Sachet de 500g", 140),
                    new Product("Poivre noir", 2.70, "Moulin de 50g", 90),
                    new Product("Papier toilette", 3.99, "Lot de 6 rouleaux", 400),
                    new Product("Essuie-tout", 2.99, "Lot de 2 rouleaux absorbants", 320),
                    new Product("Lessive liquide", 6.99, "Bouteille 1.5L pour 30 lavages", 100),
                    new Product("Éponge", 0.89, "Lot de 2 éponges cuisine", 260),
                    new Product("Sac poubelle", 2.40, "Rouleau de 20 sacs 50L", 190)
            );

            productRepository.saveAll(products);
            System.out.println("✔ Produits ajoutés (" + products.size() + ")");
        }
    }
}
