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

package io.olvid.messenger.plus_button;

import android.animation.ObjectAnimator;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import java.util.Arrays;
import java.util.regex.Matcher;

import io.olvid.engine.engine.types.JsonIdentityDetails;
import io.olvid.engine.engine.types.identities.ObvMutualScanUrl;
import io.olvid.engine.engine.types.identities.ObvUrlIdentity;
import io.olvid.messenger.App;
import io.olvid.messenger.AppSingleton;
import io.olvid.messenger.R;
import io.olvid.messenger.activities.ObvLinkActivity;
import io.olvid.messenger.customClasses.ContactCacheInfo;
import io.olvid.messenger.customClasses.InitialView;
import io.olvid.messenger.customClasses.StringUtils;
import io.olvid.messenger.databases.entity.OwnedIdentity;

public class InvitationScannedFragment extends Fragment implements View.OnClickListener  {
    AppCompatActivity activity;
    PlusButtonViewModel viewModel;

    InitialView contactInitialView;
    TextView contactNameTextView;
    TextView inviteWarningTextView;
    View cardView;
    TextView mutualScanExplanationTextView;
    ImageView mutualScanQrCodeImageView;
    TextView inviteContactButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = (AppCompatActivity) requireActivity();
        viewModel = new ViewModelProvider(activity).get(PlusButtonViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_plus_button_invitation_scanned, container, false);

        if (!viewModel.isDeepLinked()) {
            requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), new OnBackPressedCallback(true) {
                @Override
                public void handleOnBackPressed() {
                    viewModel.setMutualScanUrl(null, null);
                    Navigation.findNavController(rootView).popBackStack();
                }
            });
        }

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES) {
            activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.almostWhite));
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                activity.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                if (activity.getWindow().getStatusBarColor() == 0xff000000) {
                    ObjectAnimator.ofArgb(activity.getWindow(), "statusBarColor", activity.getWindow().getStatusBarColor(), ContextCompat.getColor(activity, R.color.almostWhite)).start();
                } else {
                    activity.getWindow().setStatusBarColor(ContextCompat.getColor(activity, R.color.almostWhite));
                }
            } else {
                ObjectAnimator.ofArgb(activity.getWindow(), "statusBarColor", activity.getWindow().getStatusBarColor(), ContextCompat.getColor(activity, R.color.olvid_gradient_dark)).start();
            }
        }

        // set screen brightness to the max
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        if (params != null && activity.getWindow() != null) {
            params.screenBrightness = 1f;
            activity.getWindow().setAttributes(params);
        }

        view.findViewById(R.id.back_button).setOnClickListener(this);

        contactInitialView = view.findViewById(R.id.contact_initial_view);
        contactNameTextView = view.findViewById(R.id.contact_name_text_view);
        inviteWarningTextView = view.findViewById(R.id.invite_warning_text_view);
        inviteContactButton = view.findViewById(R.id.invite_contact_button);
        cardView = view.findViewById(R.id.mutual_scan_card_view);
        mutualScanExplanationTextView = view.findViewById(R.id.mutual_scan_explanation_text_view);
        mutualScanQrCodeImageView = view.findViewById(R.id.qr_code_image_view);

        view.findViewById(R.id.mutual_scan_card_view).setOnClickListener(this);

        String uri = viewModel.getScannedUri();
        if (uri == null) {
            activity.finish();
            return;
        }

        ObvUrlIdentity contactUrlIdentity = null;
        Matcher matcher = ObvLinkActivity.INVITATION_PATTERN.matcher(uri);
        if (matcher.find()) {
            contactUrlIdentity = ObvUrlIdentity.fromUrlRepresentation(uri);
        }
        if (contactUrlIdentity == null || viewModel.getCurrentIdentity() == null) {
            activity.finish();
            return;
        }

        ObvUrlIdentity finalContactUrlIdentity = contactUrlIdentity;
        displayContact(viewModel.getCurrentIdentity(), finalContactUrlIdentity);
    }

    private void displayContact(@Nullable OwnedIdentity ownedIdentity, @NonNull ObvUrlIdentity contactUrlIdentity) {
        if (ownedIdentity == null) {
            return;
        }

        if (Arrays.equals(ownedIdentity.bytesOwnedIdentity, contactUrlIdentity.getBytesIdentity())) {
            viewModel.setMutualScanUrl(null, null);
            displaySelfInvite(ownedIdentity);
        } else {
            try {
                JsonIdentityDetails identityDetails = ownedIdentity.getIdentityDetails();
                viewModel.setMutualScanUrl(AppSingleton.getEngine().computeMutualScanSignedNonceUrl(
                                contactUrlIdentity.getBytesIdentity(),
                                ownedIdentity.bytesOwnedIdentity,
                                (identityDetails != null) ? identityDetails.formatDisplayName(JsonIdentityDetails.FORMAT_STRING_FIRST_LAST_POSITION_COMPANY, false) : ownedIdentity.displayName),
                        contactUrlIdentity.getBytesIdentity());
                viewModel.setDismissOnMutualScanFinished(true);
            } catch (Exception e) {
                viewModel.setMutualScanUrl(null, null);
            }
            displayContact(ownedIdentity, contactUrlIdentity, viewModel.getMutualScanUrl());
        }
    }

    private void displayContact(@NonNull OwnedIdentity ownedIdentity, @NonNull ObvUrlIdentity contactUrlIdentity, @Nullable ObvMutualScanUrl mutualScanUrl) {
        String contactShortName = StringUtils.removeCompanyFromDisplayName(contactUrlIdentity.displayName);
        contactNameTextView.setText(contactUrlIdentity.displayName);
        ContactCacheInfo contactCacheInfo = AppSingleton.getContactCacheInfo(contactUrlIdentity.getBytesIdentity());

        if (contactCacheInfo != null) {
            contactInitialView.setFromCache(contactUrlIdentity.getBytesIdentity());

            inviteWarningTextView.setVisibility(View.VISIBLE);
            inviteWarningTextView.setBackgroundResource(R.drawable.background_ok_message);
            inviteWarningTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_ok_outline, 0, 0, 0);
            if (contactCacheInfo.getOneToOne()) {
                inviteWarningTextView.setText(activity.getString(R.string.text_explanation_warning_mutual_scan_contact_already_known, contactShortName));
            } else {
                inviteWarningTextView.setText(activity.getString(R.string.text_explanation_warning_mutual_scan_contact_already_known_not_one_to_one, contactShortName));
            }
        } else {
            contactInitialView.setInitial(contactUrlIdentity.getBytesIdentity(), StringUtils.getInitial(contactUrlIdentity.displayName));
            inviteWarningTextView.setVisibility(View.GONE);
        }


        if (mutualScanUrl != null) {
            cardView.setVisibility(View.VISIBLE);
            mutualScanExplanationTextView.setText(activity.getString(R.string.text_explanation_mutual_scan, contactShortName));
            App.setQrCodeImage(mutualScanQrCodeImageView, mutualScanUrl.getUrlRepresentation());
        } else {
            cardView.setVisibility(View.GONE);
        }

        inviteContactButton.setOnClickListener(v -> {
            try {
                AppSingleton.getEngine().startTrustEstablishmentProtocol(contactUrlIdentity.getBytesIdentity(), contactUrlIdentity.displayName, ownedIdentity.bytesOwnedIdentity);
                activity.finish();
            } catch (Exception e) {
                App.toast(R.string.toast_message_failed_to_invite_contact, Toast.LENGTH_SHORT);
            }
        });
    }

    private void displaySelfInvite(@NonNull OwnedIdentity ownedIdentity) {
        contactInitialView.setOwnedIdentity(ownedIdentity);
        contactNameTextView.setText(ownedIdentity.displayName);

        inviteWarningTextView.setVisibility(View.VISIBLE);
        inviteWarningTextView.setBackgroundResource(R.drawable.background_error_message);
        inviteWarningTextView.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_error_outline, 0, 0, 0);
        inviteWarningTextView.setText(R.string.text_explanation_warning_cannot_invite_yourself);

        cardView.setVisibility(View.GONE);
        inviteContactButton.setOnClickListener(null);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.back_button) {
            activity.onBackPressed();
        } else if (id == R.id.mutual_scan_card_view) {
            if (viewModel.getMutualScanUrl() != null) {
                viewModel.setFullScreenQrCodeUrl(viewModel.getMutualScanUrl().getUrlRepresentation());
                try {
                    Navigation.findNavController(v).navigate(R.id.action_open_full_screen_qr_code);
                } catch (Exception e) {
                    // do nothing
                }
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        viewModel.setDismissOnMutualScanFinished(false);

        // restore initial brightness
        WindowManager.LayoutParams params = activity.getWindow().getAttributes();
        if (params != null && activity.getWindow() != null) {
            params.screenBrightness = -1.0f;
            activity.getWindow().setAttributes(params);
        }
    }
}
