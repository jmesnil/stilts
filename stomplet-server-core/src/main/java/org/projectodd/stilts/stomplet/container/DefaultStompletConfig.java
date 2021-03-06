/*
 * Copyright 2011 Red Hat, Inc, and individual contributors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.projectodd.stilts.stomplet.container;

import java.util.Map;

import org.projectodd.stilts.stomplet.StompletConfig;
import org.projectodd.stilts.stomplet.StompletContext;

public class DefaultStompletConfig implements StompletConfig {

    public DefaultStompletConfig(StompletContext stompletContext, Map<String,String> properties) {
        this.stompletContext = stompletContext;
        this.properties = properties;
    }
    
    @Override
    public StompletContext getStompletContext() {
        return this.stompletContext;
    }

    @Override
    public String getProperty(String name) {
        return this.properties.get(  name  );
    }

    @Override
    public String[] getPropertyNames() {
        return this.properties.keySet().toArray( new String[this.properties.size()] );
    }

    private final StompletContext stompletContext;
    private final Map<String,String> properties;
    

}
