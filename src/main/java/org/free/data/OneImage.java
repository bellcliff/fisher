package org.free.data;

import java.awt.Image;

import org.springframework.data.annotation.Id;

public class OneImage {
    @Id
    private String id;

    public OneImage(int x, int y, Image captureImage) {

    }

    int x;
    int y;
    Image captureImage;
//	  @CreatedDate
//	  private DateTime createdDate;
}
