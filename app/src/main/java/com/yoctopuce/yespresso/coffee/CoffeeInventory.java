package com.yoctopuce.yespresso.coffee;

import com.yoctopuce.yespresso.R;
import com.yoctopuce.yespresso.YoctopuceInterface;

import java.util.ArrayList;

public class CoffeeInventory {
    private static CoffeeInventory __instance;
    private final ArrayList<Coffee> _available;

    public static CoffeeInventory get() {
        if (__instance == null) {
            __instance = new CoffeeInventory();
        }
        return __instance;
    }

    public CoffeeInventory() {
        _available = new ArrayList<>(YoctopuceInterface.NB_TUBE);
        _available.add(new Coffee("Decaffeinato", "A blend of South American Arabicas reinforced with just a touch of Robusta is lightly roasted to reveal an aroma of red fruit.", 2, R.drawable.decaffeinato));
        _available.add(new Coffee("Volluto", "A pure and lightly roasted Arabica from South America, Volluto reveals sweet and biscuity flavours, reinforced by a little acidity and a fruity note.", 4, R.drawable.volluto));
        _available.add(new Coffee("Arpeggio", "A dark roast of pure South and Central American Arabicas, Arpeggio has a strong character and intense body, enhanced by cocoa notes.", 9, R.drawable.arpeggio));
        _available.add(new Coffee("Cosi", "Pure, lightly roasted East African, Central and South American Arabicas make Cosi a light-bodied espresso with refreshing citrus notes.", 3, R.drawable.cosi));
        _available.add(new Coffee("Caramelito", "The sweet flavour of caramel softens the roasted notes of the Livanto Grand Cru. This delicate gourmet marriage evokes the creaminess of soft toffee.", 6, R.drawable.caramelito));
    }

    public Coffee getCoffee(String name) {
        for (Coffee c : _available) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        return null;
    }


    public ArrayList<Coffee> getAvailableCoffee() {
        return _available;
    }

    public int distribute(String name) {
        for (int i = 0; i < _available.size(); i++) {
            Coffee c = _available.get(i);
            if (c.getName().equals(name)) {
                return i;
            }
        }
        return -1;
    }


}
