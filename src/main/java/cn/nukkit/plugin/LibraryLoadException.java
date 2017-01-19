package cn.nukkit.plugin;

/**
 * Created on 15-12-13.
 */
public class LibraryLoadException extends RuntimeException {

	private static final long serialVersionUID = 1269443720108176768L;

	public LibraryLoadException(Library library) {
        super("Load library " + library.getArtifactId() + " failed!");
    }

}
