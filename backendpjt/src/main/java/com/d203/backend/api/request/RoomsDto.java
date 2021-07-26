package com.d203.backend.api.request;

import com.d203.backend.db.entity.Rooms;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RoomsDto {
    @Getter
    public static class ListResponse {
        private Long id;
        private String name;

        public ListResponse(Rooms entity) {
            this.id = entity.getId();
            this.name = entity.getName();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class Save {
        private String name;

        @Builder
        public Save(String name) {
            this.name = name;
        }

        public Rooms toEntity() {
            return Rooms.builder()
                    .name(name)
                    .build();
        }
    }
}
