/*
 *  Olvid for Android
 *  Copyright © 2019-2024 Olvid SAS
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

package io.olvid.engine.engine.types.sync;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.olvid.engine.datatypes.Identity;
import io.olvid.engine.datatypes.UID;
import io.olvid.engine.datatypes.containers.GroupV2;
import io.olvid.engine.encoder.DecodingException;
import io.olvid.engine.encoder.Encoded;

public class ObvSyncAtom {
    public static final int TYPE_CONTACT_NICKNAME_CHANGE = 0;
    public static final int TYPE_GROUP_V1_NICKNAME_CHANGE = 1;
    public static final int TYPE_GROUP_V2_NICKNAME_CHANGE = 2;
    public static final int TYPE_CONTACT_PERSONAL_NOTE_CHANGE = 3;
    public static final int TYPE_GROUP_V1_PERSONAL_NOTE_CHANGE = 4;
    public static final int TYPE_GROUP_V2_PERSONAL_NOTE_CHANGE = 5;
    public static final int TYPE_OWN_PROFILE_NICKNAME_CHANGE = 6;
    public static final int TYPE_CONTACT_CUSTOM_HUE_CHANGE = 7;
    public static final int TYPE_CONTACT_SEND_READ_RECEIPT_CHANGE = 8;
    public static final int TYPE_GROUP_V1_SEND_READ_RECEIPT_CHANGE = 9;
    public static final int TYPE_GROUP_V2_SEND_READ_RECEIPT_CHANGE = 10;
    public static final int TYPE_PINNED_DISCUSSIONS_CHANGE = 11;
    public static final int TYPE_TRUST_CONTACT_DETAILS = 12;
    public static final int TYPE_TRUST_GROUP_V1_DETAILS = 13;
    public static final int TYPE_TRUST_GROUP_V2_DETAILS = 14;
    public static final int TYPE_SETTING_DEFAULT_SEND_READ_RECEIPTS = 15;
    public static final int TYPE_SETTING_AUTO_JOIN_GROUPS = 16;



    public final int syncType;
    private final Identity contactIdentity;
    private final byte[] groupOwnerAndUid;
    private final byte[] bytesGroupIdentifier;
    private final String stringValue;
    private final Integer integerValue;
    private final Boolean booleanValue;
    private final List<DiscussionIdentifier> discussionIdentifiers;

    private ObvSyncAtom(int syncType, Identity contactIdentity, byte[] groupOwnerAndUid, byte[] bytesGroupIdentifier, String stringValue, Integer integerValue, Boolean booleanValue, List<DiscussionIdentifier> discussionIdentifiers) {
        this.syncType = syncType;
        this.contactIdentity = contactIdentity;
        this.groupOwnerAndUid = groupOwnerAndUid;
        this.bytesGroupIdentifier = bytesGroupIdentifier;
        this.stringValue = stringValue;
        this.integerValue = integerValue;
        this.booleanValue = booleanValue;
        this.discussionIdentifiers = discussionIdentifiers;
    }

    public static ObvSyncAtom createContactNicknameChange(byte[] bytesContactIdentity, String nickname) throws DecodingException {
        return new ObvSyncAtom(TYPE_CONTACT_NICKNAME_CHANGE, Identity.of(bytesContactIdentity), null, null, nickname, null, null, null);
    }

    public static ObvSyncAtom createGroupV1NicknameChange(byte[] bytesGroupOwnerAndUid, String nickname) {
        return new ObvSyncAtom(TYPE_GROUP_V1_NICKNAME_CHANGE, null, bytesGroupOwnerAndUid, null, nickname, null, null, null);
    }

    public static ObvSyncAtom createGroupV2NicknameChange(byte[] bytesGroupV2Identifier, String nickname) throws DecodingException {
        return new ObvSyncAtom(TYPE_GROUP_V2_NICKNAME_CHANGE, null, null, bytesGroupV2Identifier, nickname, null, null, null);
    }

    public static ObvSyncAtom createContactPersonalNoteChange(byte[] bytesContactIdentity, String personalNote) throws DecodingException {
        return new ObvSyncAtom(TYPE_CONTACT_PERSONAL_NOTE_CHANGE, Identity.of(bytesContactIdentity), null, null, personalNote, null, null, null);
    }

    public static ObvSyncAtom createGroupV1PersonalNoteChange(byte[] bytesGroupOwnerAndUid, String nickname) {
        return new ObvSyncAtom(TYPE_GROUP_V1_PERSONAL_NOTE_CHANGE, null, bytesGroupOwnerAndUid, null, nickname, null, null, null);
    }

    public static ObvSyncAtom createGroupV2PersonalNoteChange(byte[] bytesGroupV2Identifier, String nickname) throws DecodingException {
        return new ObvSyncAtom(TYPE_GROUP_V2_PERSONAL_NOTE_CHANGE, null, null, bytesGroupV2Identifier, nickname, null, null, null);
    }

    public static ObvSyncAtom createOwnProfileNicknameChange(String nickname) {
        return new ObvSyncAtom(TYPE_OWN_PROFILE_NICKNAME_CHANGE, null, null, null, nickname, null, null, null);
    }

    public static ObvSyncAtom createContactCustomHueChange(byte[] bytesContactIdentity, Integer customHue) throws DecodingException {
        return new ObvSyncAtom(TYPE_CONTACT_CUSTOM_HUE_CHANGE, Identity.of(bytesContactIdentity), null, null, null, customHue, null, null);
    }

    public static ObvSyncAtom createContactSendReadReceiptChange(byte[] bytesContactIdentity, Boolean sendReadReceipt) throws DecodingException {
        return new ObvSyncAtom(TYPE_CONTACT_SEND_READ_RECEIPT_CHANGE, Identity.of(bytesContactIdentity), null, null, null, null, sendReadReceipt, null);
    }

    public static ObvSyncAtom createGroupV1SendReadReceiptChange(byte[] bytesGroupOwnerAndUid, Boolean sendReadReceipt) {
        return new ObvSyncAtom(TYPE_GROUP_V1_SEND_READ_RECEIPT_CHANGE, null, bytesGroupOwnerAndUid, null, null, null, sendReadReceipt, null);
    }

    public static ObvSyncAtom createGroupV2SendReadReceiptChange(byte[] bytesGroupV2Identifier, Boolean sendReadReceipt) throws DecodingException {
        return new ObvSyncAtom(TYPE_GROUP_V2_SEND_READ_RECEIPT_CHANGE, null, null, bytesGroupV2Identifier, null, null, sendReadReceipt, null);
    }

    public static ObvSyncAtom createPinnedDiscussionsChange(List<DiscussionIdentifier> discussionIdentifiers, boolean ordered) throws DecodingException {
        return new ObvSyncAtom(TYPE_PINNED_DISCUSSIONS_CHANGE, null, null, null, null, null, ordered, discussionIdentifiers);
    }

    // we send the complete details to trust in the ObvSyncAtom as the version may be meaningless (after a channel creation, published details may require a version number downgrade)
    public static ObvSyncAtom createTrustContactDetails(Identity contactIdentity, String serializedIdentityDetailsWithVersionAndPhoto) {
        return new ObvSyncAtom(TYPE_TRUST_CONTACT_DETAILS, contactIdentity, null, null, serializedIdentityDetailsWithVersionAndPhoto, null, null, null);
    }

    // we send the complete details to trust in the ObvSyncAtom as the version may be meaningless (after a channel creation, published details may require a version number downgrade)
    public static ObvSyncAtom createTrustGroupV1Details(byte[] bytesGroupOwnerAndUid, String serializedGroupDetailsWithVersionAndPhoto) {
        return new ObvSyncAtom(TYPE_TRUST_GROUP_V1_DETAILS, null, bytesGroupOwnerAndUid, null, serializedGroupDetailsWithVersionAndPhoto, null, null, null);
    }

    public static ObvSyncAtom createTrustGroupV2Details(GroupV2.Identifier groupIdentifier, int version) {
        return new ObvSyncAtom(TYPE_TRUST_GROUP_V2_DETAILS, null, null, groupIdentifier.encode().getBytes(), null, version, null, null);
    }

    public static ObvSyncAtom createSettingDefaultSendReadReceipts(boolean sendReadReceipt) {
        return new ObvSyncAtom(TYPE_SETTING_DEFAULT_SEND_READ_RECEIPTS, null, null, null, null, null, sendReadReceipt, null);
    }

    public static ObvSyncAtom createSettingAutoJoinGroups(String autoJoinGroupsType) {
        return new ObvSyncAtom(TYPE_SETTING_AUTO_JOIN_GROUPS, null, null, null, autoJoinGroupsType, null, null, null);
    }


    public boolean isAppSyncItem() {
        switch (syncType) {
            case TYPE_CONTACT_NICKNAME_CHANGE:
            case TYPE_GROUP_V1_NICKNAME_CHANGE:
            case TYPE_GROUP_V2_NICKNAME_CHANGE:
            case TYPE_CONTACT_PERSONAL_NOTE_CHANGE:
            case TYPE_GROUP_V1_PERSONAL_NOTE_CHANGE:
            case TYPE_GROUP_V2_PERSONAL_NOTE_CHANGE:
            case TYPE_OWN_PROFILE_NICKNAME_CHANGE:
            case TYPE_CONTACT_CUSTOM_HUE_CHANGE:
            case TYPE_CONTACT_SEND_READ_RECEIPT_CHANGE:
            case TYPE_GROUP_V1_SEND_READ_RECEIPT_CHANGE:
            case TYPE_GROUP_V2_SEND_READ_RECEIPT_CHANGE:
            case TYPE_PINNED_DISCUSSIONS_CHANGE:
            case TYPE_SETTING_DEFAULT_SEND_READ_RECEIPTS:
            case TYPE_SETTING_AUTO_JOIN_GROUPS:
                return true;
            case TYPE_TRUST_CONTACT_DETAILS:
            case TYPE_TRUST_GROUP_V1_DETAILS:
            case TYPE_TRUST_GROUP_V2_DETAILS:
            default:
                return false;
        }
    }

    public byte[] getBytesContactIdentity() {
        return contactIdentity.getBytes();
    }

    public Identity getContactIdentity() {
        return contactIdentity;
    }

    public byte[] getBytesGroupOwnerAndUid() {
        return groupOwnerAndUid;
    }

    public byte[] getBytesGroupIdentifier() {
        return bytesGroupIdentifier;
    }

    public GroupV2.Identifier getGroupIdentifier() throws DecodingException {
        return GroupV2.Identifier.of(bytesGroupIdentifier);
    }

    public String getStringValue() {
        if (stringValue == null) {
            return null;
        }
        String out = stringValue.trim();
        if (out.length() == 0) {
            return null;
        }
        return out;
    }

    public Integer getIntegerValue() {
        return integerValue;
    }

    public Boolean getBooleanValue() {
        return booleanValue;
    }

    public List<DiscussionIdentifier> getDiscussionIdentifiers() {
        return discussionIdentifiers;
    }

    public Encoded encode() {
        ArrayList<Encoded> encodeds = new ArrayList<>();
        encodeds.add(Encoded.of(syncType));
        switch (syncType) {
            case TYPE_CONTACT_NICKNAME_CHANGE:
            case TYPE_CONTACT_PERSONAL_NOTE_CHANGE: {
                encodeds.add(Encoded.of(contactIdentity));
                if (stringValue != null) {
                    encodeds.add(Encoded.of(stringValue));
                }
                break;
            }
            case TYPE_GROUP_V1_NICKNAME_CHANGE:
            case TYPE_GROUP_V1_PERSONAL_NOTE_CHANGE:  {
                encodeds.add(Encoded.of(Arrays.copyOfRange(groupOwnerAndUid, 0, groupOwnerAndUid.length - UID.UID_LENGTH)));
                encodeds.add(Encoded.of(Arrays.copyOfRange(groupOwnerAndUid, groupOwnerAndUid.length - UID.UID_LENGTH, groupOwnerAndUid.length)));
                if (stringValue != null) {
                    encodeds.add(Encoded.of(stringValue));
                }
                break;
            }
            case TYPE_GROUP_V2_NICKNAME_CHANGE:
            case TYPE_GROUP_V2_PERSONAL_NOTE_CHANGE: {
                encodeds.add(Encoded.of(bytesGroupIdentifier));
                if (stringValue != null) {
                    encodeds.add(Encoded.of(stringValue));
                }
                break;
            }
            case TYPE_OWN_PROFILE_NICKNAME_CHANGE: {
                if (stringValue != null) {
                    encodeds.add(Encoded.of(stringValue));
                }
                break;
            }
            case TYPE_CONTACT_CUSTOM_HUE_CHANGE: {
                encodeds.add(Encoded.of(contactIdentity));
                if (integerValue != null) {
                    encodeds.add(Encoded.of(integerValue));
                }
                break;
            }
            case TYPE_CONTACT_SEND_READ_RECEIPT_CHANGE: {
                encodeds.add(Encoded.of(contactIdentity));
                if (booleanValue != null) {
                    encodeds.add(Encoded.of(booleanValue));
                }
                break;
            }
            case TYPE_GROUP_V1_SEND_READ_RECEIPT_CHANGE: {
                encodeds.add(Encoded.of(Arrays.copyOfRange(groupOwnerAndUid, 0, groupOwnerAndUid.length - UID.UID_LENGTH)));
                encodeds.add(Encoded.of(Arrays.copyOfRange(groupOwnerAndUid, groupOwnerAndUid.length - UID.UID_LENGTH, groupOwnerAndUid.length)));
                if (booleanValue != null) {
                    encodeds.add(Encoded.of(booleanValue));
                }
                break;
            }
            case TYPE_GROUP_V2_SEND_READ_RECEIPT_CHANGE: {
                encodeds.add(Encoded.of(bytesGroupIdentifier));
                if (booleanValue != null) {
                    encodeds.add(Encoded.of(booleanValue));
                }
                break;
            }
            case TYPE_PINNED_DISCUSSIONS_CHANGE: {
                List<Encoded> encodedDiscussionIdentifiers = new ArrayList<>();
                for (DiscussionIdentifier discussionIdentifier : discussionIdentifiers) {
                    encodedDiscussionIdentifiers.add(discussionIdentifier.encode());
                }
                encodeds.add(Encoded.of(encodedDiscussionIdentifiers.toArray(new Encoded[0])));
                encodeds.add(Encoded.of(booleanValue));
                break;
            }
            case TYPE_TRUST_CONTACT_DETAILS: {
                encodeds.add(Encoded.of(contactIdentity));
                encodeds.add(Encoded.of(stringValue));
                break;
            }
            case TYPE_TRUST_GROUP_V1_DETAILS: {
                encodeds.add(Encoded.of(Arrays.copyOfRange(groupOwnerAndUid, 0, groupOwnerAndUid.length - UID.UID_LENGTH)));
                encodeds.add(Encoded.of(Arrays.copyOfRange(groupOwnerAndUid, groupOwnerAndUid.length - UID.UID_LENGTH, groupOwnerAndUid.length)));
                encodeds.add(Encoded.of(stringValue));
                break;
            }
            case TYPE_TRUST_GROUP_V2_DETAILS: {
                encodeds.add(Encoded.of(bytesGroupIdentifier));
                encodeds.add(Encoded.of(integerValue));
                break;
            }
            case TYPE_SETTING_DEFAULT_SEND_READ_RECEIPTS: {
                encodeds.add(Encoded.of(booleanValue));
                break;
            }
            case TYPE_SETTING_AUTO_JOIN_GROUPS: {
                encodeds.add(Encoded.of(stringValue));
                break;
            }
            default: {
                return null;
            }
        }
        return Encoded.of(encodeds.toArray(new Encoded[0]));
    }

    public static ObvSyncAtom of(Encoded encoded) throws DecodingException {
        Encoded[] encodeds = encoded.decodeList();
        if (encodeds.length == 0) {
            throw new DecodingException();
        }
        int syncType = (int) encodeds[0].decodeLong();
        switch (syncType) {
            case TYPE_CONTACT_NICKNAME_CHANGE:
            case TYPE_CONTACT_PERSONAL_NOTE_CHANGE: {
                if (encodeds.length == 2) {
                    return new ObvSyncAtom(syncType, encodeds[1].decodeIdentity(), null, null, null, null, null, null);
                } else if (encodeds.length == 3) {
                    return new ObvSyncAtom(syncType, encodeds[1].decodeIdentity(), null, null, encodeds[2].decodeString(), null, null, null);
                }
                break;
            }
            case TYPE_GROUP_V1_NICKNAME_CHANGE:
            case TYPE_GROUP_V1_PERSONAL_NOTE_CHANGE: {
                if (encodeds.length == 3) {
                    return new ObvSyncAtom(syncType, null, joinArrays(encodeds[1].decodeBytes(), encodeds[2].decodeBytes()), null, null, null, null, null);
                } else if (encodeds.length == 4) {
                    return new ObvSyncAtom(syncType, null, joinArrays(encodeds[1].decodeBytes(), encodeds[2].decodeBytes()), null, encodeds[3].decodeString(), null, null, null);
                }
                break;
            }
            case TYPE_GROUP_V2_NICKNAME_CHANGE:
            case TYPE_GROUP_V2_PERSONAL_NOTE_CHANGE: {
                if (encodeds.length == 2) {
                    return new ObvSyncAtom(syncType, null, null, encodeds[1].decodeBytes(), null, null, null, null);
                } else if (encodeds.length == 3) {
                    return new ObvSyncAtom(syncType, null, null, encodeds[1].decodeBytes(), encodeds[2].decodeString(), null, null, null);
                }
                break;
            }
            case TYPE_OWN_PROFILE_NICKNAME_CHANGE: {
                if (encodeds.length == 1) {
                    return new ObvSyncAtom(syncType, null, null, null, null, null, null, null);
                } else if (encodeds.length == 2) {
                    return new ObvSyncAtom(syncType, null, null, null, encodeds[1].decodeString(), null, null, null);
                }
                break;
            }
            case TYPE_CONTACT_CUSTOM_HUE_CHANGE: {
                if (encodeds.length == 2) {
                    return new ObvSyncAtom(syncType, encodeds[1].decodeIdentity(), null, null, null, null, null, null);
                } else if (encodeds.length == 3) {
                    return new ObvSyncAtom(syncType, encodeds[1].decodeIdentity(), null, null, null, (int) encodeds[2].decodeLong(), null, null);
                }
                break;
            }
            case TYPE_CONTACT_SEND_READ_RECEIPT_CHANGE: {
                if (encodeds.length == 2) {
                    return new ObvSyncAtom(syncType, encodeds[1].decodeIdentity(), null, null, null, null, null, null);
                } else if (encodeds.length == 3) {
                    return new ObvSyncAtom(syncType, encodeds[1].decodeIdentity(), null, null, null, null, encodeds[2].decodeBoolean(), null);
                }
                break;
            }
            case TYPE_GROUP_V1_SEND_READ_RECEIPT_CHANGE: {
                if (encodeds.length == 3) {
                    return new ObvSyncAtom(syncType, null, joinArrays(encodeds[1].decodeBytes(), encodeds[2].decodeBytes()), null, null, null, null, null);
                } else if (encodeds.length == 4) {
                    return new ObvSyncAtom(syncType, null, joinArrays(encodeds[1].decodeBytes(), encodeds[2].decodeBytes()), null, null, null, encodeds[3].decodeBoolean(), null);
                }
                break;
            }
            case TYPE_GROUP_V2_SEND_READ_RECEIPT_CHANGE: {
                if (encodeds.length == 2) {
                    return new ObvSyncAtom(syncType, null, null, encodeds[1].decodeBytes(), null, null, null, null);
                } else if (encodeds.length == 3) {
                    return new ObvSyncAtom(syncType, null, null, encodeds[1].decodeBytes(), null, null, encodeds[2].decodeBoolean(), null);
                }
                break;
            }
            case TYPE_PINNED_DISCUSSIONS_CHANGE: {
                List<DiscussionIdentifier> discussionIdentifiers = new ArrayList<>();
                for (Encoded encodedDiscussionIdentifier : encodeds[1].decodeList()) {
                    discussionIdentifiers.add(DiscussionIdentifier.of(encodedDiscussionIdentifier));
                }
                return new ObvSyncAtom(syncType, null, null, null, null, null, encodeds[2].decodeBoolean(), discussionIdentifiers);
            }
            case TYPE_TRUST_CONTACT_DETAILS: {
                return new ObvSyncAtom(syncType, encodeds[1].decodeIdentity(), null, null, encodeds[2].decodeString(), null, null, null);
            }
            case TYPE_TRUST_GROUP_V1_DETAILS: {
                return new ObvSyncAtom(syncType, null, joinArrays(encodeds[1].decodeBytes(), encodeds[2].decodeBytes()), null, encodeds[3].decodeString(), null, null, null);
            }
            case TYPE_TRUST_GROUP_V2_DETAILS: {
                return new ObvSyncAtom(syncType, null, null, encodeds[1].decodeBytes(), null, (int) encodeds[2].decodeLong(), null, null);
            }
            case TYPE_SETTING_DEFAULT_SEND_READ_RECEIPTS: {
                return new ObvSyncAtom(syncType, null, null, null, null, null, encodeds[1].decodeBoolean(), null);
            }
            case TYPE_SETTING_AUTO_JOIN_GROUPS: {
                return new ObvSyncAtom(syncType, null, null, null, encodeds[1].decodeString(), null, null, null);
            }
        }
        throw new DecodingException();
    }

    private static byte[] joinArrays(byte[] a, byte[] b) {
        byte[] out = new byte[a.length + b.length];
        System.arraycopy(a, 0, out, 0, a.length);
        System.arraycopy(b, 0, out, a.length, b.length);
        return out;
    }

    public static class DiscussionIdentifier {
        public static final int CONTACT = 0;
        public static final int GROUP_V1 = 1;
        public static final int GROUP_V2 = 2;

        public final int type;
        public final byte[] bytesDiscussionIdentifier;

        public DiscussionIdentifier(int type, byte[] bytesDiscussionIdentifier) {
            this.type = type;
            this.bytesDiscussionIdentifier = bytesDiscussionIdentifier;
        }

        public Encoded encode() {
            List<Encoded> encodeds = new ArrayList<>();
            encodeds.add(Encoded.of(type));
            switch (type) {
                case CONTACT:
                case GROUP_V2: {
                    encodeds.add(Encoded.of(bytesDiscussionIdentifier));
                    break;
                }
                case GROUP_V1: {
                    encodeds.add(Encoded.of(Arrays.copyOfRange(bytesDiscussionIdentifier, 0, bytesDiscussionIdentifier.length - UID.UID_LENGTH)));
                    encodeds.add(Encoded.of(Arrays.copyOfRange(bytesDiscussionIdentifier, bytesDiscussionIdentifier.length - UID.UID_LENGTH, bytesDiscussionIdentifier.length)));
                    break;
                }
                default:
                    return null;
            }
            return Encoded.of(encodeds.toArray(new Encoded[0]));
        }


        public static DiscussionIdentifier of(Encoded encoded) throws DecodingException {
            Encoded[] encodeds = encoded.decodeList();
            if (encodeds.length == 0) {
                throw new DecodingException();
            }
            int type = (int) encodeds[0].decodeLong();
            switch (type) {
                case CONTACT:
                case GROUP_V2: {
                    return new DiscussionIdentifier(type, encodeds[1].decodeBytes());
                }
                case GROUP_V1: {
                    return new DiscussionIdentifier(type, joinArrays(encodeds[1].decodeBytes(), encodeds[2].decodeBytes()));
                }
            }
            throw new DecodingException();
        }
    }
}
