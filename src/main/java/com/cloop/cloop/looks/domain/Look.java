package com.cloop.cloop.looks.domain;

import com.cloop.cloop.auth.domain.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name="look")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Look {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long lookId;

    @Column(nullable = false)
    private String photo;

    @Column(nullable = false)
    private Date createdAt;

    // User와 FK 관계 설정
    @ManyToOne
    @JoinColumn(name="userId", nullable = false)
    private User user;

}
