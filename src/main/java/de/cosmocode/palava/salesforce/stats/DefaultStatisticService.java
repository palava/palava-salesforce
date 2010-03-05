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

package de.cosmocode.palava.salesforce.stats;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import de.cosmocode.palava.concurrent.ScheduledService;
import de.cosmocode.palava.salesforce.SalesforceScheduler;

/**
 * Dummy statistic service which uses a {@link ScheduledService}.
 *
 * @author Willi Schoenborn
 */
final class DefaultStatisticService extends ScheduledService {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultStatisticService.class);

    private final ScheduledExecutorService scheduler;
    
    @Inject
    public DefaultStatisticService(@SalesforceScheduler ScheduledExecutorService scheduler) {
        this.scheduler = Preconditions.checkNotNull(scheduler, "Scheduler");
    }

    @Inject(optional = true)
    @Override
    protected void setAutostart(@Named(StatisticServiceConfig.AUTOSTART) boolean autostart) {
        super.setAutostart(autostart);
    }

    @Inject(optional = true)
    @Override
    protected void setMonth(@Named(StatisticServiceConfig.MONTH) int month) {
        super.setMonth(month);
    }

    @Inject(optional = true)
    @Override
    protected void setWeek(@Named(StatisticServiceConfig.WEEK) int week) {
        super.setWeek(week);
    }

    @Inject
    @Override
    protected void setDay(@Named(StatisticServiceConfig.DAY) int day) {
        super.setDay(day);
    }
    
    @Inject
    @Override
    protected void setHour(@Named(StatisticServiceConfig.HOUR) int hour) {
        super.setHour(hour);
    }

    @Inject(optional = true)
    @Override
    protected void setMinute(@Named(StatisticServiceConfig.MINUTE) int minute) {
        super.setMinute(minute);
    }

    @Inject
    @Override
    protected void setPeriod(@Named(StatisticServiceConfig.PERIOD) long period) {
        super.setPeriod(period);
    }

    @Inject
    @Override
    protected void setPeriodUnit(@Named(StatisticServiceConfig.PERIOD_UNIT) TimeUnit periodUnit) {
        super.setPeriodUnit(periodUnit);
    }
    
    @Override
    protected ScheduledExecutorService getScheduler() {
        return scheduler;
    }
    
    @Override
    public void run() {
        // do work here
    }
    
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        LOG.error("Uncaught exception in " + t, e);
    }
    
}
