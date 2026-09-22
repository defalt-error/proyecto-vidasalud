package cl.duoc.vidasalud.catalog.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "boxes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Box {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String code; // e.g., BOX-101, BOX-DENTAL-01

    @Column(nullable = false)
    private String name;

    private String centerId; // e.g., CENTRO-SANTIAGO-CENTRO

    private String specialty; // e.g., Medicina General, Dental, Pediatría

    @Builder.Default
    private Boolean active = true;
}
