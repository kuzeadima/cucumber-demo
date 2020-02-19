package com.thekuzea.experimental.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "t_publication")
public class Publication {

    @Id
    @GeneratedValue
    @Type(type = "uuid-char")
    private UUID id;

    @Column(name = "topic")
    private String topic;

    @Column(name = "body")
    private String body;

    @Column(name = "publication_time")
    private OffsetDateTime publicationTime;

    @ManyToOne
    @JoinColumn(name = "published_by")
    private User publishedBy;
}
