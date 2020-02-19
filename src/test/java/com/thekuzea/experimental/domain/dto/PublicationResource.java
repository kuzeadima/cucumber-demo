package com.thekuzea.experimental.domain.dto;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class PublicationResource {

    String id;

    String publishedBy;

    String publicationTime;

    String topic;

    String body;
}
