package com.robotbot.financetracker.domain.usecases.settings

import com.robotbot.financetracker.domain.repotisories.ApplicationSettingsRepository
import javax.inject.Inject

class GetAllSettingsUseCase @Inject constructor(
    private val applicationSettingsRepository: ApplicationSettingsRepository
) {

    operator fun invoke() = applicationSettingsRepository.getAllSettings()
}