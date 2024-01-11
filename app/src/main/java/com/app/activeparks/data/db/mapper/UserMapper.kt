package com.app.activeparks.data.db.mapper

import com.app.activeparks.data.db.entity.UserEntity
import com.app.activeparks.data.model.registration.User

/**
 * Created by O.Dziuba on 28.11.2023.
 */
class UserMapper {
    companion object {
        fun mapToUserEntity(user: User): UserEntity {
            return UserEntity(
                id = user.id,
                birthday = user.birthday,
                isPhoneVerified = user.isPhoneVerified,
                regions = user.regions,
                city = user.city,
                hideBodyInfo = user.hideBodyInfo,
                aboutMe = user.aboutMe,
                createdAt = user.createdAt,
                lastActiveCoordinates = user.lastActiveCoordinates,
                updatedAt = user.updatedAt,
                roleId = user.roleId,
                nickname = user.nickname,
                firstName = user.firstName,
                subRole = user.subRole,
                email = user.email,
                height = user.height,
                healthState = user.healthState,
                isActive = user.isActive,
                sex = user.sex,
                regionId = user.regionId,
                lastName = user.lastName,
                weight = user.weight,
                photo = user.photo,
                imageBackground = user.imageBackground,
                isTrainer = user.isTrainer,
                buildId = user.buildId,
                phone = user.phone,
                secondName = user.secondName,
                districtId = user.districtId,
                position = user.position
            )
        }

        fun mapToUser(userEntity: UserEntity): User {
            return User(
                id = userEntity.id,
                birthday = userEntity.birthday,
                isPhoneVerified = userEntity.isPhoneVerified,
                regions = userEntity.regions,
                city = userEntity.city,
                hideBodyInfo = userEntity.hideBodyInfo,
                aboutMe = userEntity.aboutMe,
                createdAt = userEntity.createdAt,
                lastActiveCoordinates = userEntity.lastActiveCoordinates,
                updatedAt = userEntity.updatedAt,
                roleId = userEntity.roleId,
                nickname = userEntity.nickname,
                firstName = userEntity.firstName,
                subRole = userEntity.subRole,
                email = userEntity.email,
                height = userEntity.height,
                healthState = userEntity.healthState,
                isActive = userEntity.isActive,
                sex = userEntity.sex,
                regionId = userEntity.regionId,
                lastName = userEntity.lastName,
                weight = userEntity.weight,
                photo = userEntity.photo,
                imageBackground = userEntity.imageBackground,
                isTrainer = userEntity.isTrainer,
                buildId = userEntity.buildId,
                phone = userEntity.phone,
                secondName = userEntity.secondName,
                districtId = userEntity.districtId,
                position = userEntity.position
            )
        }
    }
}