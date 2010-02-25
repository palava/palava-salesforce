/**
 * palava - a java-php-bridge
 * Copyright (C) 2007-2010  CosmoCode GmbH
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package de.cosmocode.palava.salesforce.sync;

import java.util.concurrent.Executors;

import org.easymock.EasyMock;

import com.google.inject.Binder;
import com.google.inject.Module;

import de.cosmocode.palava.concurrent.ThreadProvider;
import de.cosmocode.palava.core.RegistryModule;

public final class SyncTestApplication implements Module {

    @Override
    public void configure(Binder binder) {
        binder.install(new SyncServiceModule());
        binder.install(new RegistryModule());
        final ThreadProvider provider = EasyMock.createMock("provider", ThreadProvider.class);
        EasyMock.expect(provider.newThreadFactory()).andReturn(Executors.defaultThreadFactory());
        EasyMock.replay(provider);
        binder.bind(ThreadProvider.class).toInstance(provider);
    }

}
