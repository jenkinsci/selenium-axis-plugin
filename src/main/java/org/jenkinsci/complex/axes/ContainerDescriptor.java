package org.jenkinsci.complex.axes;

import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

/**
 * Created with IntelliJ IDEA.
 * User: jeremym
 * Date: 9/13/13
 * Time: 3:02 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ContainerDescriptor extends ItemDescriptor {

    public ContainerDescriptor() {
        load();
    }

    @Override
    public boolean configure(StaplerRequest req, JSONObject formData) throws FormException {
        // To persist global configuration information,
        // set that to properties and call save().
        req.bindJSON(this, formData);
        save();
        return true;
    }
}
