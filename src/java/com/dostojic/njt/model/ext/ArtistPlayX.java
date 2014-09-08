/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dostojic.njt.model.ext;

import com.dostojic.njt.model.Artist;
import com.dostojic.njt.model.ArtistPlay;

/**
 *
 * @author dejan
 */
public class ArtistPlayX extends ArtistPlay{
    
    private Artist artist;

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }
    
    
    
}
