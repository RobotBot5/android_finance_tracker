package com.robotbot.financetracker.domain.repotisories

import com.robotbot.financetracker.domain.entities.TransactionCategoryEntity

interface CategoryRepository : CrudRepository<TransactionCategoryEntity> {
}