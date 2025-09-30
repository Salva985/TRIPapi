package com.tripapi.dto.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PagedResponse<T> {
    private List<T> data;
    private Meta meta;

@Data
@NoArgsConstructor
@AllArgsConstructor
public static class Meta {
    private int page;
    private int pageSize;
    private long total;
  }
}
