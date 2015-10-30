/*
 * Copyright 2015 David Karnok
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See
 * the License for the specific language governing permissions and limitations under the License.
 */

package hu.akarnokd.xml;

import java.util.*;
import java.util.function.Supplier;


/**
 * Utility classes to handle XSerializable object creation and conversion.
 */
public final class XSerializables {
    /**
     * Utility class.
     */
    private XSerializables() {
    }
    /**
     * Create a request with the given function and name-value pairs.
     * @param function the remote function name
     * @param nameValue the array of String name and Object attributes.
     * @return the request XML
     */
    public static XElement createRequest(String function, Object... nameValue) {
        XElement result = new XElement(function);
        for (int i = 0; i < nameValue.length; i += 2) {
            result.set((String)nameValue[i], nameValue[i + 1]);
        }
        return result;
    }
    /**
     * Create an update request and store the contents of the object into it.
     * @param function the remote function name
     * @param object the object to store.
     * @return the request XML
     */
    public static XElement createUpdate(String function, XSerializable object) {
        XElement result = new XElement(function);
        object.save(result);
        return result;
    }
    /**
     * Parses an container for the given itemName elements and loads them into the
     * given Java XSerializable object.
     * @param <T> the element object type
     * @param container the container XElement
     * @param itemName the item name
     * @param creator the function to create Ts
     * @return the list of elements
     */
    public static <T extends XSerializable> List<T> parseList(XElement container, 
            String itemName, Supplier<T> creator) {
        List<T> result = new ArrayList<>();
        for (XElement e : container.childrenWithName(itemName)) {
            T obj = creator.get();
            obj.load(e);
            result.add(obj);
        }
        return result;
    }
    /**
     * Create an XSerializable object through the {@code creator} function
     * and load it from the {@code item}.
     * @param <T> the XSerializable object
     * @param item the item to load from
     * @param creator the function to create Ts
     * @return the created and loaded object
     */
    public static <T extends XSerializable> T parseItem(XElement item, Supplier<T> creator) {
        T result = creator.get();
        result.load(item);
        return result;
    }
    /**
     * Create an XElement with the given name and items stored from the source sequence.
     * @param container the container name
     * @param item the item name
     * @param source the source of items
     * @return the list in XElement
     */
    public static XElement storeList(String container, String item, Iterable<? extends XSerializable> source) {
        XElement result = new XElement(container);
        for (XSerializable e : source) {
            e.save(result.add(item));
        }
        return result;
    }
    /**
     * Store the value of a single serializable object with the given element name.
     * @param itemName the item element name
     * @param source the object to store
     * @return the created XElement
     */
    public static XElement storeItem(String itemName, XSerializable source) {
        return createUpdate(itemName, source);
    }
}
