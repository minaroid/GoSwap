
package swap.go.george.mina.goswap.rest.apiModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class ItemPics {

    private ArrayList<String> itemPics = null;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public ArrayList<String> getItemPics() {
        return itemPics;
    }

    public void setItemPics(ArrayList<String> itemPics) {
        this.itemPics = itemPics;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
