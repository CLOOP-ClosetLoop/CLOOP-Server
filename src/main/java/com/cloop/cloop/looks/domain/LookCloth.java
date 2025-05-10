package com.cloop.cloop.looks.domain;

import com.cloop.cloop.clothes.domain.Cloth;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="look_cloth")
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LookCloth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lookClothId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lookId")
    private Look look;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clothId")
    private Cloth cloth;

}
