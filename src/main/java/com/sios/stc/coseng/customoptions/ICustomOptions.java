/*
 * Concurrent Selenium TestNG (COSENG)
 * Copyright (c) 2013-2018 SIOS Technology Corp.  All rights reserved.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sios.stc.coseng.customoptions;

import java.net.URI;

import com.sios.stc.coseng.gson.Serializers;
import com.sios.stc.coseng.run.Test;

public interface ICustomOptions {

    public void validateAndPrepare(Test test, Serializers serializers, URI configResource);

    public Serializers getSerializers();

}