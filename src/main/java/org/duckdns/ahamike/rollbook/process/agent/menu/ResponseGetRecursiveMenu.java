package org.duckdns.ahamike.rollbook.process.agent.menu;

import java.util.ArrayList;
import java.util.List;

import org.duckdns.ahamike.rollbook.table.MenuEntity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseGetRecursiveMenu {
    private Long id;
    private String name;
    private Integer ordering;
    private List<ResponseGetRecursiveMenu> children = new ArrayList<>();

    public static ResponseGetRecursiveMenu from(MenuEntity m) {
        return new ResponseGetRecursiveMenu(
                m.getId(),
                m.getName(),
                m.getOrdering(),
                new ArrayList<>()
        );
    }
}
