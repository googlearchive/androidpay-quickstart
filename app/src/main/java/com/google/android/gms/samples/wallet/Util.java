/*
 * Copyright Google Inc. All Rights Reserved.
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

package com.google.android.gms.samples.wallet;

import android.content.Context;

/**
 * Helper util methods.
 */
public class Util {

    /**
     * Formats a price for display.
     *
     * @param context The context to get String resources from.
     * @param priceMicros The price to display, in micros.
     * @return The given price in a format suitable for display to the user.
     */
    static String formatPrice(Context context, long priceMicros) {
        return context.getString(R.string.price_format, priceMicros / 1000000d);
    }
}
