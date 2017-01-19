package cn.nukkit.plugin.certification;

import java.io.Serializable;

/**
 * Created by iNevet.
 */
public abstract class PluginCertificate extends Thread implements Serializable {

	private static final long serialVersionUID = -3367694554790117945L;

	protected transient boolean localCertificated = false;

    protected String certificate;
    protected boolean certificated;
    protected String encryptType;

}
