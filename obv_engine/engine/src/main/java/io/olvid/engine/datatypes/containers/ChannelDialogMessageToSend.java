/*
 *  Olvid for Android
 *  Copyright © 2019-2022 Olvid SAS
 *
 *  This file is part of Olvid for Android.
 *
 *  Olvid is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Affero General Public License, version 3,
 *  as published by the Free Software Foundation.
 *
 *  Olvid is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Affero General Public License for more details.
 *
 *  You should have received a copy of the GNU Affero General Public License
 *  along with Olvid.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.olvid.engine.datatypes.containers;


import java.util.UUID;

import io.olvid.engine.datatypes.Identity;
import io.olvid.engine.encoder.Encoded;

public class ChannelDialogMessageToSend implements ChannelMessageToSend {
    private final UUID uuid;
    private final SendChannelInfo sendChannelInfo;
    private final Encoded encodedElements;

    public ChannelDialogMessageToSend(UUID uuid, Identity toIdentity, DialogType dialogType, Encoded encodedElements) {
        this.uuid = uuid;
        this.sendChannelInfo = SendChannelInfo.createUserInterfaceChannelInfo(toIdentity, dialogType, uuid);
        this.encodedElements = encodedElements;
    }

    @Override
    public int getMessageType() {
        return MessageType.DIALOG_MESSAGE_TYPE;
    }

    @Override
    public SendChannelInfo getSendChannelInfo() {
        return sendChannelInfo;
    }

    public Encoded getEncodedElements() {
        return encodedElements;
    }

    public UUID getUuid() {
        return uuid;
    }
}
