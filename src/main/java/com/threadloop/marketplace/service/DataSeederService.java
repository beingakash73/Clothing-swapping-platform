package com.threadloop.marketplace.service;

import com.threadloop.marketplace.model.*;
import com.threadloop.marketplace.repository.*;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DataSeederService {

    private final UserRepository userRepository;
    private final BadgeRepository badgeRepository;
    private final ClothingItemRepository clothingItemRepository;
    private final SwapProposalRepository swapProposalRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final DisputeRepository disputeRepository;
    private final MeetupHubRepository meetupHubRepository;
    private final PlatformKPIRepository platformKPIRepository;

    public DataSeederService(UserRepository userRepository,
                             BadgeRepository badgeRepository,
                             ClothingItemRepository clothingItemRepository,
                             SwapProposalRepository swapProposalRepository,
                             ChatMessageRepository chatMessageRepository,
                             DisputeRepository disputeRepository,
                             MeetupHubRepository meetupHubRepository,
                             PlatformKPIRepository platformKPIRepository) {
        this.userRepository = userRepository;
        this.badgeRepository = badgeRepository;
        this.clothingItemRepository = clothingItemRepository;
        this.swapProposalRepository = swapProposalRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.disputeRepository = disputeRepository;
        this.meetupHubRepository = meetupHubRepository;
        this.platformKPIRepository = platformKPIRepository;
    }

    @PostConstruct
    public void init() {
        if (userRepository.count() == 0) {
            seedDatabase();
        }
    }

    @Transactional
    public void seedDatabase() {
        // Clear existing records
        chatMessageRepository.deleteAll();
        disputeRepository.deleteAll();
        swapProposalRepository.deleteAll();
        clothingItemRepository.deleteAll();
        badgeRepository.deleteAll();
        userRepository.deleteAll();
        meetupHubRepository.deleteAll();
        platformKPIRepository.deleteAll();

        // 1. Seed Users
        User maya = new User(
                "user_maya", "Maya Lin", "maya.lin@threadloop.org", "user",
                "https://images.unsplash.com/photo-1534528741775-53994a69daeb?auto=format&fit=crop&w=400&q=80",
                "Circular fashion enthusiast & stylist. Believer in zero textile waste and wardrobe rotation.",
                new Location("Brooklyn", "NY", "11201", 40.6928, -73.9903),
                4.95, 28, 16, 860, 43200, 88.0, 24.0, "2025-04-10"
        );
        userRepository.save(maya);

        badgeRepository.save(new Badge("b1", maya, "Circular Pioneer", "Sparkles", "Completed 15+ successful garment trades", "2025-11-12"));
        badgeRepository.save(new Badge("b2", maya, "Zero-Waste Champion", "Leaf", "Diverted over 20kg of textiles from landfills", "2026-01-05"));
        badgeRepository.save(new Badge("b3", maya, "Speedy Shipper", "Truck", "Dispatches within 24h of agreement", "2026-02-18"));

        User leo = new User(
                "user_leo", "Leo Thorne", "leo.thorne@threadloop.org", "user",
                "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
                "Archivist of 70s-90s vintage denim, military outerwear, and heritage wool garments.",
                new Location("Manhattan", "NY", "10002", 40.715, -73.986),
                5.0, 34, 22, 920, 59400, 121.0, 33.0, "2025-02-14"
        );
        userRepository.save(leo);

        badgeRepository.save(new Badge("b4", leo, "Vintage Curator", "Clock", "10+ curated vintage items traded", "2025-08-20"));
        badgeRepository.save(new Badge("b5", leo, "Master Negotiator", "Scale", "Maintains a 100% fair-swap score", "2025-10-15"));

        User sofia = new User(
                "user_sofia", "Sofia Rossi", "sofia.rossi@threadloop.org", "user",
                "https://images.unsplash.com/photo-1517841905240-472988babdf9?auto=format&fit=crop&w=400&q=80",
                "Minimalist designer swapping seasonal capsule pieces. Lover of Scandinavian knitwear and French tailoring.",
                new Location("Queens", "NY", "11101", 40.744, -73.948),
                4.88, 15, 9, 710, 24300, 49.5, 13.5, "2025-06-22"
        );
        userRepository.save(sofia);

        badgeRepository.save(new Badge("b6", sofia, "Capsule Curator", "Layers", "Swapped across all 4 seasons", "2026-03-01"));

        User admin = new User(
                "user_admin", "Sarah Connor (Admin)", "moderation@threadloop.org", "admin",
                "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=400&q=80",
                "Head of Trust, Safety & Circular Operations at ThreadLoop.",
                new Location("Brooklyn", "NY", "11201", 40.6928, -73.9903),
                5.0, 88, 40, 1200, 108000, 220.0, 60.0, "2024-01-01"
        );
        userRepository.save(admin);

        badgeRepository.save(new Badge("b7", admin, "Platform Moderator", "ShieldCheck", "Certified circular economy steward", "2024-01-01"));

        // 2. Seed Meetup Hubs
        meetupHubRepository.save(new MeetupHub("hub_1", "Brooklyn Central Library Eco Hub", "10 Grand Army Plaza, Brooklyn, NY 11238", "safe_hub", 40.6728, -73.9686));
        meetupHubRepository.save(new MeetupHub("hub_2", "Union Square Green Market Kiosk", "Broadway & E 17th St, Manhattan, NY 10003", "community_center", 40.7359, -73.9911));
        meetupHubRepository.save(new MeetupHub("hub_3", "Court Square Collective Café", "45-20 Court Square W, Long Island City, NY 11101", "cafe", 40.7473, -73.9442));
        meetupHubRepository.save(new MeetupHub("hub_4", "Williamsburg Transit Hub Safe Zone", "Bedford Ave & N 7th St, Brooklyn, NY 11211", "transit_hub", 40.7181, -73.9576));

        // 3. Seed Clothing Items
        ClothingItem item1 = new ClothingItem(
                "item_1", "Classic Retro-X Deep Pile Fleece Jacket",
                "Windproof, warm fleece jacket made of 85% recycled polyester. Natural pelican colorway with navy chest pocket. Worn gently for one winter season in upstate NY.",
                "Patagonia", "designer_sustainable", "Jackets & Coats", "Fleece Outerwear",
                "M", "Unisex", "like_new", "Flawless condition, zero matting, zipper glides smoothly.",
                "85% Recycled Polyester Fleece", "Natural / Pelican Navy", 229.0, 165.0,
                List.of("https://images.unsplash.com/photo-1544441893-675973e31985?auto=format&fit=crop&w=1000&q=80", "https://images.unsplash.com/photo-1578587018452-892bacefd3f2?auto=format&fit=crop&w=1000&q=80"),
                maya, "available", List.of("Sustainable", "Winter", "Gorpcore", "Cozy"), 7.2, 3200.0, "2026-03-01T10:00:00Z"
        );
        clothingItemRepository.save(item1);

        ClothingItem item2 = new ClothingItem(
                "item_2", "Sigmund Square-Neck Linen Midi Dress",
                "Effortless romantic linen dress with high slit, button detailing, and smocked back. Breathable 100% organic linen.",
                "Reformation", "designer_sustainable", "Dresses & Jumpsuits", "Midi Dress",
                "S", "Women", "new_with_tags", "Brand new with original tags attached. Never worn.",
                "100% Organic Linen", "Sage Eucalyptus", 278.0, 195.0,
                List.of("https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?auto=format&fit=crop&w=1000&q=80", "https://images.unsplash.com/photo-1496747611176-843222e1e57c?auto=format&fit=crop&w=1000&q=80"),
                maya, "available", List.of("Summer", "Linen", "Sustainable", "Cocktail"), 6.5, 2800.0, "2026-03-02T14:30:00Z"
        );
        clothingItemRepository.save(item2);

        ClothingItem item3 = new ClothingItem(
                "item_3", "Grade-A Cashmere Relaxed Crew Sweater",
                "Incredibly soft Mongolian Grade-A cashmere. Clean crew neck, ribbed cuffs and hem. Beautiful camel melange.",
                "Everlane", "premium", "Sweaters & Knitwear", "Cashmere Pullover",
                "M", "Women", "gently_used", "Worn 3-4 times. Carefully dry-cleaned, no pilling or moth damage.",
                "100% Grade-A Cashmere", "Camel Oat", 175.0, 95.0,
                List.of("https://images.unsplash.com/photo-1576566588028-4147f3842f27?auto=format&fit=crop&w=1000&q=80"),
                maya, "in_negotiation", List.of("Cashmere", "Minimalist", "Workwear"), 5.1, 2400.0, "2026-02-28T09:15:00Z"
        );
        clothingItemRepository.save(item3);

        ClothingItem item4 = new ClothingItem(
                "item_4", "Vintage 1954 501 Redline Selvedge Denim",
                "Authentic 1950s reissue Big E redline selvedge denim. Zip fly, cone mills raw denim with natural honeycombs and vintage fading.",
                "Levi's Vintage Clothing", "vintage", "Pants & Denim", "Straight Selvedge Jeans",
                "32x32", "Men", "gently_used", "Superb natural patina, solid stitching, chain-stitched hem.",
                "100% Cone Mills Cotton Denim", "Indigo Fade", 285.0, 185.0,
                List.of("https://images.unsplash.com/photo-1542272604-780c96856592?auto=format&fit=crop&w=1000&q=80", "https://images.unsplash.com/photo-1582552938357-32b906df40cb?auto=format&fit=crop&w=1000&q=80"),
                leo, "available", List.of("Vintage", "Selvedge", "Heritage", "Denim"), 9.8, 4200.0, "2026-03-03T11:20:00Z"
        );
        clothingItemRepository.save(item4);

        ClothingItem item5 = new ClothingItem(
                "item_5", "Canada Oversized Virgin Wool Fringe Scarf",
                "Iconic wide fringed scarf crafted in Italy from 100% virgin wool with pink brand patch. Ultra warm and statement piece.",
                "Acne Studios", "designer_sustainable", "Bags & Accessories", "Winter Scarf",
                "One Size", "Unisex", "like_new", "Worn twice, carefully stored in dustbag. Fresh and crisp.",
                "100% Virgin Wool", "Oatmeal Melange", 240.0, 160.0,
                List.of("https://images.unsplash.com/photo-1520903920243-00d872a2d1c9?auto=format&fit=crop&w=1000&q=80"),
                leo, "in_negotiation", List.of("Luxury", "Winter", "Accessories", "Minimalist"), 4.2, 1900.0, "2026-03-01T16:45:00Z"
        );
        clothingItemRepository.save(item5);

        ClothingItem item6 = new ClothingItem(
                "item_6", "Beaufort Waxed Cotton Field Jacket",
                "Classic British heritage thornproof waxed cotton jacket with corduroy collar, double storm fly front, and large bellows pockets.",
                "Barbour", "premium", "Jackets & Coats", "Waxed Jacket",
                "L", "Men", "worn_with_love", "Glorious vintage patina and character. Freshly re-waxed with authentic Barbour wax.",
                "100% Waxed Cotton", "Sage Olive", 425.0, 190.0,
                List.of("https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=1000&q=80"),
                leo, "available", List.of("Heritage", "Waxed", "Rainwear", "Classic"), 11.0, 4800.0, "2026-02-25T12:00:00Z"
        );
        clothingItemRepository.save(item6);

        ClothingItem item7 = new ClothingItem(
                "item_7", "Gaspard Reversible Mohair Cardigan",
                "Parisian staple cardigan with pearl buttons, soft ribbed trim, and round neckline. Can be worn with buttons front or back.",
                "Sézane", "premium", "Sweaters & Knitwear", "Cardigan",
                "S", "Women", "like_new", "Worn once for photos. Soft and fluffy.",
                "37% Super Kid Mohair, 37% Alpaca, 26% Polyamide", "Powder Ecru", 145.0, 105.0,
                List.of("https://images.unsplash.com/photo-1434389677669-e08b4cac3105?auto=format&fit=crop&w=1000&q=80"),
                sofia, "available", List.of("French", "Knitwear", "Chic", "Parisian"), 4.8, 2100.0, "2026-03-02T18:10:00Z"
        );
        clothingItemRepository.save(item7);

        ClothingItem item8 = new ClothingItem(
                "item_8", "Heavyweight Boiled Wool Overshirt",
                "Architectural boxy silhouette in dense boiled wool. Clean concealed placket, chest patch pockets, straight hem.",
                "COS", "premium", "Jackets & Coats", "Wool Overshirt",
                "M", "Unisex", "like_new", "Pristine condition, no flaws.",
                "100% RWS Certified Wool", "Dark Navy Charcoal", 190.0, 125.0,
                List.of("https://images.unsplash.com/photo-1516257984-b1b4d707412e?auto=format&fit=crop&w=1000&q=80"),
                sofia, "available", List.of("Minimalist", "Wool", "Layering"), 6.0, 2700.0, "2026-03-03T08:30:00Z"
        );
        clothingItemRepository.save(item8);

        ClothingItem item9 = new ClothingItem(
                "item_9", "Campo ChromeFree Leather Low-Top Sneakers",
                "Eco-conscious Brazilian leather sneakers with wild Amazonian rubber soles and recycled jersey lining.",
                "Veja", "designer_sustainable", "Footwear", "Sneakers",
                "EU 39 / US 8.5", "Women", "gently_used", "Light crease at toe box, soles have 95% tread remaining. Washed insoles.",
                "ChromeFree Leather & Amazonian Rubber", "Extra White / Natural", 175.0, 95.0,
                List.of("https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?auto=format&fit=crop&w=1000&q=80"),
                sofia, "available", List.of("Sustainable", "Sneakers", "Streetwear"), 5.4, 3100.0, "2026-03-01T09:00:00Z"
        );
        clothingItemRepository.save(item9);

        ClothingItem item10 = new ClothingItem(
                "item_10", "Detroit Blanket-Lined Duck Canvas Work Jacket",
                "Heavy 12oz organic duck canvas with striped blanket lining and corduroy top collar. Snap cuffs and drop-tail hem.",
                "Carhartt WIP", "premium", "Jackets & Coats", "Workwear Jacket",
                "L", "Men", "like_new", "Firm duck canvas starting to break in nicely. No scuffs.",
                "100% Organic Cotton Canvas", "Hamilton Brown", 238.0, 155.0,
                List.of("https://images.unsplash.com/photo-1548883354-7622d03aca27?auto=format&fit=crop&w=1000&q=80"),
                admin, "available", List.of("Workwear", "Streetwear", "Canvas"), 8.5, 3800.0, "2026-02-27T15:00:00Z"
        );
        clothingItemRepository.save(item10);

        ClothingItem item11 = new ClothingItem(
                "item_11", "Vintage 1460 Smooth Leather 8-Eye Boots",
                "Made in England vintage Dr. Martens with yellow welt stitching, grooved air-cushioned soles, and smooth black leather.",
                "Dr. Martens", "vintage", "Footwear", "Combat Boots",
                "UK 8 / US Men 9", "Unisex", "gently_used", "Naturally softened leather, broken in so zero heel blisters. AirWair sole in great shape.",
                "100% Quilon Leather", "Black Heritage", 210.0, 135.0,
                List.of("https://images.unsplash.com/photo-1520639888713-7851133b1ed0?auto=format&fit=crop&w=1000&q=80"),
                leo, "available", List.of("Grunge", "Boots", "Vintage", "Punk"), 7.9, 3400.0, "2026-03-04T07:15:00Z"
        );
        clothingItemRepository.save(item11);

        ClothingItem item12 = new ClothingItem(
                "item_12", "Pleated Recycled Twill Wide-Leg Trousers",
                "High-waisted tailored trousers with deep front pleats, slant pockets, and a fluid wide-leg drape.",
                "Arket", "premium", "Pants & Denim", "Pleated Trousers",
                "EU 38 / US 6", "Women", "like_new", "Only worn for one conference presentation. Freshly steamed.",
                "Wool and Recycled Polyester Blend", "Melange Grey", 150.0, 90.0,
                List.of("https://images.unsplash.com/photo-1509631179647-0177331693ae?auto=format&fit=crop&w=1000&q=80"),
                sofia, "available", List.of("Tailoring", "Office", "Scandi", "Minimalist"), 4.6, 2200.0, "2026-03-02T11:00:00Z"
        );
        clothingItemRepository.save(item12);

        // 4. Seed Swap Proposals
        MeetupLocation hubLoc1 = new MeetupLocation("Brooklyn Central Library Eco Hub", "10 Grand Army Plaza, Brooklyn, NY 11238", "safe_hub", 40.6728, -73.9686);
        SwapProposal swap1 = new SwapProposal(
                "swap_01", maya, leo, item5, List.of("item_3"), "negotiating", "local_meetup", hubLoc1,
                null, null,
                "Hi Leo! Love your Acne scarf. Would you be open to swapping for my Grade-A camel cashmere sweater? I can also add a vintage silk bandana if needed to balance the value!",
                68.0, 65.0, null, null,
                "2026-03-03T14:20:00Z", "2026-03-04T09:10:00Z"
        );
        swapProposalRepository.save(swap1);

        SwapProposal swap2 = new SwapProposal(
                "swap_02", sofia, maya, item2, List.of("item_7", "item_12"), "pending", "courier_shipping", null,
                null, null,
                "Hello Maya! I've been searching everywhere for the Sigmund dress in sage. I'm offering my Sézane Gaspard cardigan + Arket wide trousers (totaling $195 value) for a 2-for-1 fair swap!",
                100.0, 0.0, null, null,
                "2026-03-04T16:00:00Z", "2026-03-04T16:00:00Z"
        );
        swapProposalRepository.save(swap2);

        MeetupLocation hubLoc2 = new MeetupLocation("Union Square Green Market Kiosk", "Broadway & E 17th St, Manhattan, NY 10003", "community_center", 40.7359, -73.9911);
        SwapProposal swap3 = new SwapProposal(
                "swap_03", leo, maya, item1, List.of("item_4"), "completed", "local_meetup", hubLoc2,
                null, null,
                "Hey Maya, traded my 501 selvedge for your fleece. Met at Union Square green market. Perfect swap!",
                92.0, -20.0, "2026-02-28T18:00:00Z", "2026-02-28T18:05:00Z",
                "2026-02-26T10:00:00Z", "2026-02-28T18:05:00Z"
        );
        swapProposalRepository.save(swap3);

        // 5. Seed Chat Messages
        chatMessageRepository.save(new ChatMessage("msg_01", "swap_01", "user_maya", "Hi Leo! Love your Acne scarf. Would you be open to swapping for my Grade-A camel cashmere sweater? I can also add a vintage silk bandana if needed to balance the value!", "2026-03-03T14:20:00Z", false, null));
        chatMessageRepository.save(new ChatMessage("msg_02", "swap_01", "user_leo", "Hey Maya! That cashmere sweater looks gorgeous and super clean. Since the Acne scarf is $160 estimated and the sweater is around $95, adding that vintage silk bandana or doing a local coffee pickup would be great!", "2026-03-03T16:45:00Z", false, null));
        chatMessageRepository.save(new ChatMessage("msg_03", "swap_01", "user_maya", "That sounds completely fair! I can meet you at the Brooklyn Central Library Eco Hub this Friday around 3 PM.", "2026-03-04T09:10:00Z", false, null));
        chatMessageRepository.save(new ChatMessage("msg_04", "swap_02", "user_sofia", "Hello Maya! I've been searching everywhere for the Sigmund dress in sage. I'm offering my Sézane Gaspard cardigan + Arket wide trousers (totaling $195 value) for a 2-for-1 fair swap!", "2026-03-04T16:00:00Z", false, null));

        // 6. Seed Disputes
        disputeRepository.save(new Dispute(
                "disp_01", "swap_01", sofia, leo, "item_condition_mismatch",
                "The denim jacket received had minor seam tear at the cuff that was not mentioned in the listing description.",
                "under_review",
                "Under review by Sarah Connor. Contacted seller for replacement garment or mutual return shipping voucher.",
                "2026-03-02T12:00:00Z"
        ));

        // 7. Seed Platform KPIs
        platformKPIRepository.save(new PlatformKPI(
                "global", 14820, 4390, 8740, 12450.0, 23600000.0, 48070.0, 94.8
        ));
    }
}
