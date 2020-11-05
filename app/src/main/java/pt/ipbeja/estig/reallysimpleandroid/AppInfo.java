package pt.ipbeja.estig.reallysimpleandroid;

import android.graphics.drawable.Drawable;

/**
 * The type App info.
 */
public class AppInfo {
    private long id;
    private CharSequence label;
    private CharSequence packageName;
    private Drawable icon;
    private boolean isAllowed;

    /**
     * Instantiates a new App info.
     */
    public AppInfo() {
    }

    /**
     * Instantiates a new App info.
     *
     * @param id          the id
     * @param label       the label
     * @param packageName the package name
     * @param icon        the icon
     */
    public AppInfo(long id, CharSequence label, CharSequence packageName, Drawable icon) {
        this.id = id;
        this.label = label;
        this.packageName = packageName;
        this.icon = icon;
        this.isAllowed = false;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gets allowed boolean.
     *
     * @return the boolean
     */
    public boolean isAllowed() {
        return isAllowed;
    }

    /**
     * Sets allowed.
     *
     * @param allowed the allowed
     */
    public void setAllowed(boolean allowed) {
        isAllowed = allowed;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public long getId() {
        return id;
    }

    /**
     * Gets label.
     *
     * @return the label
     */
    public CharSequence getLabel() {
        return label;
    }

    /**
     * Sets label.
     *
     * @param label the label
     */
    public void setLabel(CharSequence label) {
        this.label = label;
    }

    /**
     * Gets package name.
     *
     * @return the package name
     */
    public CharSequence getPackageName() {
        return packageName;
    }

    /**
     * Sets package name.
     *
     * @param packageName the package name
     */
    public void setPackageName(CharSequence packageName) {
        this.packageName = packageName;
    }

    /**
     * Gets icon.
     *
     * @return the icon
     */
    public Drawable getIcon() {
        return icon;
    }

    /**
     * Sets icon.
     *
     * @param icon the icon
     */
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }
}