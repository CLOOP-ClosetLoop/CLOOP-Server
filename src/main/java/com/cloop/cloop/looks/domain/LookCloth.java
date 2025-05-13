package com.cloop.cloop.looks.domain;

import com.cloop.cloop.clothes.domain.Cloth;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="look_cloth")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LookCloth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "look_id")
    private Look look;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cloth_id")
    private Cloth cloth;
}