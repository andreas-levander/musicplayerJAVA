package model;


import java.util.ArrayList;
import java.util.List;

public class Album implements Albums {

    private String name;
    private List<SoundClip> album;
    private List<Album> children;
    private Album parent;
    private boolean recentlymodified;

    public Album(String name) {
        children = new ArrayList<>();
        album = new ArrayList<>();
        parent = null;
        this.name = name;
        recentlymodified = false;

    }
    /** adds the soundclip to the Album and all parentalbums if the parentalbum does not contain it */
    public void add(SoundClip soundclip) {
        if(!album.contains(soundclip)) {
            album.add(soundclip);
            setRecentlymodified(true);
            if (parent != null && !parent.contains(soundclip)) {
                parent.add(soundclip);
            }
        }

    }
    /** removes the SoundClip from the album and all subalbums*/
    public void remove(SoundClip soundclip) {
    	if (parent != null && album.contains(soundclip)) {
    		album.remove(soundclip);
    		setRecentlymodified(true);
    		for (Album subalbum : children) {
    			subalbum.remove(soundclip);
    		}
    	}
    }
    /** adds a childalbum and sets its parent to this album*/
    public void addchild(Album album) {
        children.add(album);
        album.setParent(this);
    }
    /** removes childalbum and sets its children's parents to this album */
    public void removechild(Album album) {
        children.remove(album);
        for (Album subalbum : album.children) {
            this.addchild(subalbum);
            
        }
    }
    /** returns the name of the Album */
    public String getName() {
        return name;
    }
    public String toString(){ return name; }
    /** returns the size of the Album */
    public int getSize() {
        return album.size();
    }
    /** getter for recentlymodified */
    public boolean getRecentlymodified() { return recentlymodified; }
    /** sets the Boolean recentlymodified of the Album */
    public void setRecentlymodified(boolean set) {
        recentlymodified = set;
    }
    /** returns the SoundClip at index i in the Album*/
    public SoundClip get(int i) {
        return album.get(i);
    }
    /** returns the List album*/
    public List<SoundClip> getList() { return album;}
    /** sets the Album's parent to the album parameter */
    public void setParent(Album album) {
        parent = album;
    }
    /** returns the parent of the Album */
    public Album getParent() {
        return parent;
    }
    /** returns the children of the Album */
    public List<Album> getChildren() { return children;}
    /** for testing  */
    public boolean containChild(Album album) {
        return children.contains(album);
    }
    /** checks if the Album contains a SoundClip */
    public boolean contains(SoundClip soundclip) {
        return album.contains(soundclip);
    }
    /** checks if the Album contains another Album*/
    public boolean contains(Album album) {
        for (int i = 0;i<album.getSize();i++) {
            if (!this.album.contains(album.get(i))) {
                return false;
            }
        }
        return true;

    }
    public boolean invariant() {
        return name != null &&
                !name.equals("") &&
                parent != this &&
                parent != null &&
                this.getParent().contains(this);
    }
}
